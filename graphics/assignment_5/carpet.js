var layers = [];
var BASE = 0;
const SHIFT = 2/3;
var direction = 1;
var is_rotating = false;
var theta = 0;
var new_time = 0;
var old_time = 0;
var delta = 0;
var theta_var = 0;
var eyeCamX;
var eyeCamY;
var eyeCamZ;
var centCamX;
var centCamY;
var lightInt;

var texCoordArr;
var normalsArr;
var lightDirX;
var lightDirY;
var lightDirZ;

/* Need to add:
	- light (parallel or point)
	- material (random or uniform on each square)
	- camera motion
*/

//-1 means light is pointing directly opposite at the object
// 1 means light is pointing directly at the object
// multiply color by dot product to produce light (Phong light equation)
// normals - describe the direction a surface is facing (perpendicular)

window.onload = function main(){
	run();
}

function run(){
	var num_items = 0;
	var parent = [];
	var vertices = [];
	var centers = [];
	var tmp_vertices = [];
	texCoordArr = [];
	normalsArr = [];

	var canvas = document.getElementById('web_gl_canvas');
	var depth = document.getElementById('depth').value;
	eyeCamX = document.getElementById('eyeCamX').value;
	eyeCamY = document.getElementById('eyeCamY').value;
	eyeCamZ = document.getElementById('eyeCamZ').value;
	centCamX = document.getElementById('centCamX').value;
	centCamY = document.getElementById('centCamY').value;
	lightInt = document.getElementById('lightInt').value;
 	lightDirX = document.getElementById('lightDirX').value;
 	lightDirY = document.getElementById('lightDirY').value;
	//lightDirZ = document.getElementById('lightDirZ').value;

	var gl = init(canvas);
	canvas.onclick = change_direction;

	var vert_shader = load_vertex_shader(gl);
	var frag_shader = load_frag_shader(gl);
	var program = link_program(gl, canvas, vert_shader, frag_shader);

	layers = [];
	BASE = 1/3;
	parent = [];
	get_verts(parent, depth, true);
	tmp_vertices = fill(tmp_vertices);

	layers = [];
	BASE = 0;
	parent = [];
	get_verts(parent, depth, true);
	centers = fill(centers);

	data = tmp_vertices.concat(centers); // same amount of normals as there are vertices
	num_items = (tmp_vertices.length / 8); // number of squares

	var texCoord = [
		1,1,
		1,0,
		0,0,
		0,1
	];

	var normals = [
      0.0, 0.0,
      0.0, 0.0,
      0.0 ,0.0,
      0.0 ,0.0
   ];

   	var texCount = 0;
	for (i = 0; i < num_items; i++){
		texCount++;
		data = data.concat(texCoord);
	}

	var texLen = texCount * texCoord.length;

	for (i = 0; i < num_items; i++){
		data = data.concat(normals);
	}

	console.log(data);
	console.log(texLen);
	create_buffer(gl, data, program, canvas, num_items, tmp_vertices, centers, texLen);
}

function change_direction(){
	direction = direction * -1;
}

function change_rotation(){
	if (is_rotating == false){
		is_rotating = true;
	} else {
		is_rotating = false;
	}
}

function init(canvas){	
	try {
		var gl = canvas.getContext('webgl');
	} catch (e1) {
		try {
			gl = canvas.getContext('experimental-webl');
		} catch (e2){
			throw new Error('no Webgl found.');
		}
	}
	return gl;
}

function load_vertex_shader(gl){
	var vert_code = document.getElementById('vertex_shader').textContent;
	var vertex_shader = gl.createShader(gl.VERTEX_SHADER);
	gl.shaderSource(vertex_shader, vert_code);
	gl.compileShader(vertex_shader);
	
	// check if everything compiled correctly
	if (!gl.getShaderParameter(vertex_shader, gl.COMPILE_STATUS)){
		throw new Error(gl.getShaderInfoLog(vertex_shader));
	}
	return vertex_shader;
}

function load_frag_shader(gl){
	var frag_code = document.getElementById('fragment_shader').textContent;
	var fragment_shader = gl.createShader(gl.FRAGMENT_SHADER);
	gl.shaderSource(fragment_shader, frag_code);
	gl.compileShader(fragment_shader);
	
	// check if everything compiled correctly
	if (!gl.getShaderParameter(fragment_shader, gl.COMPILE_STATUS)){
		throw new Error(gl.getShaderInfoLog(fragment_shader));
	}
	return fragment_shader;
}

function link_program(gl, canvas, vert_shader, frag_shader){
	// Put vertex and fragment shaders together into complete program
	var program = gl.createProgram();
	gl.attachShader(program, vert_shader);
	gl.attachShader(program, frag_shader);
	gl.linkProgram(program);
	
	// make sure program was created correctly
	if (!gl.getProgramParameter(program, gl.LINK_STATUS)){
		throw new Error(gl.getProgramInfoLog(program));
	}

	gl.viewport(0, 0, canvas.width, canvas.height);
	gl.clearColor(0.65, 0.7, 0.65, 1.0);
	return program;
}

function create_buffer(gl, data, program, canvas, num_items, vertices, centers, texLen){
	gl.useProgram(program);

	var buffer = gl.createBuffer(); // set aside memory on GPU for data
	gl.bindBuffer(gl.ARRAY_BUFFER, buffer); // select this buffer as something to manipulate
	gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(data), gl.STATIC_DRAW); //copy data to buffer

	var normalVar = gl.getAttribLocation(program, "vertNormal");
	gl.enableVertexAttribArray(normalVar);

	gl.vertexAttribPointer(
		normalVar,
		2,
		gl.FLOAT,
		false,
		0,
		(vertices.length*4) + (centers.length*4) + (texLen*4)
	);

	var coordinates_var = gl.getAttribLocation(program, "coordinates");
	gl.enableVertexAttribArray(coordinates_var);

	gl.vertexAttribPointer(
		coordinates_var, 
		2, 
		gl.FLOAT, 
		false, 
		0, 
		0
	);

	var center_var = gl.getAttribLocation(program, "center");
	gl.enableVertexAttribArray(center_var);

	gl.vertexAttribPointer(
		center_var, 
		2, 
		gl.FLOAT, 
		false, 
		0, 
		vertices.length*4
	);

	var texCoordVar = gl.getAttribLocation(program, 'vertTexCoord');
	gl.enableVertexAttribArray(texCoordVar);

	gl.vertexAttribPointer(
		texCoordVar, // attribute location
		2, // elements per attribute
		gl.FLOAT, // type
		false,
		0,
		(vertices.length*4) + (centers.length*4)
	);

	// create texture
	var texture;/*var texture = gl.createTexture();
	gl.bindTexture(gl.TEXTURE_2D, texture); // bind to GPU
	gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE); // s - u, t - v
	gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE);
	gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR);
	gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.LINEAR);

	gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, document.getElementById("box_image"));

	gl.bindTexture(gl.TEXTURE_2D, null); // unbind
	*/
	// grab theta from the shader
	theta_var = gl.getUniformLocation(program, "theta");

	// get projection matrix from shader and fill with identity
	var matProjectionVar = gl.getUniformLocation(program, "mProj");
	var matViewVar = gl.getUniformLocation(program, "mView");

	var viewMatrix = new Float32Array(16);
	var projMatrix = new Float32Array(16);

	// generate look at matrix     eye			  			    center       		  up
	mat4.lookAt(viewMatrix, [eyeCamX, eyeCamY, eyeCamZ], [centCamX, centCamY, 0], [0, 1, 0]);
	mat4.perspective(projMatrix, glMatrix.toRadian(45), canvas.width / canvas.height, 0.1, 1000.0);

	gl.uniformMatrix4fv(matProjectionVar, gl.FALSE, projMatrix);
	gl.uniformMatrix4fv(matViewVar, gl.FALSE, viewMatrix);

	/* Lighting Information */
	gl.useProgram(program);
	var ambientLightIntensityVar = gl.getUniformLocation(program, 'ambientLightIntensity');
	var directionalLightDirVar = gl.getUniformLocation(program, 'directionalLightDirection');
	var directionalLightColorVar = gl.getUniformLocation(program, 'directionalLightColor');

	gl.uniform3f(ambientLightIntensityVar, lightInt, lightInt, lightInt);
	gl.uniform3f(directionalLightDirVar, lightDirX, lightDirY, -2.0);
	gl.uniform3f(directionalLightColorVar, 0.1, 0.2, 0.2);

	rotate(gl, num_items, texture);
}

function rotate(gl, num_items, texture) {
	var loop = function(){
		old_time = new_time;
		new_time = performance.now();
		delta = new_time - old_time;
		
		if (is_rotating){
			theta += (direction * (delta / 500 / 6 * 2 *  Math.PI)); // miliseconds since window loaded
		}

		// send theta to the shader
		gl.uniform1f(theta_var, theta);
		render(gl, num_items, texture);
		requestAnimationFrame(loop);
	};
	requestAnimationFrame(loop);
}

function render(gl, num_items, texture){
	// draw to the screen
	gl.clear(gl.DEPTH_BUFFER_BIT | gl.COLOR_BUFFER_BIT);
	var offset = 0;
	//gl.bindTexture(gl.TEXTURE_2D, texture);
	//gl.activeTexture(gl.TEXTURE0);
	for (i = 0; i < num_items; i++){
		gl.drawArrays(gl.TRIANGLE_FAN, offset, 4);
		offset += 4;
	}
}

function fill(arr){
	for (i = 0; i < layers.length; i++){
		arr = arr.concat(layers[i]);
	}
	return arr;
}

function get_verts(parent, depth, is_base){
	if (is_base){
		var base = [
			//X  Y	     U, V
			-BASE,-BASE,
			-BASE,BASE, 
			BASE,BASE, 
			BASE,-BASE   
		];
		parent.push(base);
		layers.push(base);	
	}

	if (depth == 0){
		return;
	}
	
	// parent length is 8^layer#
	var children = [];
	for (k = 0; k < parent.length; k++){
		for (i = 0; i < 8; i++){
			var child = [];
			var vert_to_add;
			for (j = 0; j < parent[k].length; j++){
				vert = parent[k][j] / 3;
				switch (i){
					case 0:
						// bottom_left (0,0)
						vert_to_add = vert - SHIFT;
						child.push(vert_to_add);
						break;
					case 1:
						// side left()
						if (j % 2 == 0){
							// X
							vert_to_add = vert - SHIFT;
							child.push(vert_to_add)
						} else {
							// Y
							child.push(vert);
						}
						break;
					case 2:
						// top left
						if (j % 2 == 0){
							// X
							vert_to_add = vert - SHIFT;
							child.push(vert_to_add)
						} else {
							// Y
							vert_to_add = vert + SHIFT;	
							child.push(vert_to_add);
						}
						break;
					case 3:
						// top middle
						if (j % 2 == 0){
							child.push(vert)
						} else {
							vert_to_add = vert + SHIFT;
							child.push(vert_to_add); 	
						}
						break;
					case 4:
						//top_right
						vert_to_add = vert + SHIFT;
						child.push(vert_to_add);
						break;
					case 5:
						// side right
						if (j % 2 == 0){
							// X
							vert_to_add = vert + SHIFT;
							child.push(vert_to_add)
						} else {
							// Y
							child.push(vert);
						}
						break;
					case 6:
						// bottom right
						if (j % 2 == 0){
							// X
							vert_to_add = vert + SHIFT;
							child.push(vert_to_add)
						} else {
							// Y
							vert_to_add = vert - SHIFT; 	
							child.push(vert_to_add);
						}
						break;
					case 7:
						//bottom middle
						if (j % 2 == 0){
							child.push(vert)
						} else {
							vert_to_add = vert - SHIFT;
							child.push(vert_to_add);	
						}
						break;
					default:
						console.log("something went wrong");
				}
			}

			children.push(child);
			//texCoordArr = texCoordArr.concat(texCoord);
			layers.push(child);
		}
	}

	depth--;
	get_verts(children, depth, false);
	return;
}
