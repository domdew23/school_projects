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

var texCoordArr;
var normalsArr;
var AmbR;
var AmbG;
var AmbB;
var diffR;
var diffG;
var diffB;
var specR;
var specG;
var specB;
var matAmbx;
var matAmby;
var matAmbz;
var matDifx;
var matDify;
var matDifz;
var matSpecx;
var matSpecy;
var matSpecz;
var matShine;
var lightX;
var lightY;
var lightZ;

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

	var canvas = document.getElementById('web_gl_canvas');
	var depth = document.getElementById('depth').value;
	eyeCamX = document.getElementById('eyeCamX').value;
	eyeCamY = document.getElementById('eyeCamY').value;
	eyeCamZ = document.getElementById('eyeCamZ').value;
	centCamX = document.getElementById('centCamX').value;
	centCamY = document.getElementById('centCamY').value;
	AmbR = document.getElementById('AmbR').value;
 	AmbG = document.getElementById('AmbG').value;
 	AmbB = document.getElementById('AmbB').value;
 	diff = document.getElementById('diff').value;
	spec = document.getElementById('spec').value;
	matAmbx = document.getElementById('matAmbx').value;
	matAmby = document.getElementById('matAmby').value;
	matAmbz = document.getElementById('matAmbz').value;
	matDif = document.getElementById('matDif').value;
	matSpec = document.getElementById('matSpec').value;
	matShine = document.getElementById('matShine').value;
	lightX = document.getElementById('lightX').value;
	lightY = document.getElementById('lightY').value;
	lightZ = document.getElementById('lightZ').value;

	var gl = init(canvas);
	canvas.onclick = change_direction;

	var vert_shader = load_vertex_shader(gl);
	var frag_shader = load_frag_shader(gl);
	var program = link_program(gl, canvas, vert_shader, frag_shader);

	layers = [];
	BASE = 1/3;
	parent = [];
	get_verts(parent, depth, true);
	vertices = fill(vertices);

	layers = [];
	BASE = 0;
	parent = [];
	get_verts(parent, depth, true);
	centers = fill(centers);

	data = vertices.concat(centers); // same amount of normals as there are vertices
	num_items = (vertices.length / 8); // number of squares

	var normals = [
      1.0, 1.0,
      1.0, 1.0,
      1.0 ,1.0,
      1.0 ,1.0
   ];

	for (i = 0; i < num_items; i++){
		data = data.concat(normals);
	}

	create_buffer(gl, data, program, canvas, num_items, vertices, centers);
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

function hi(){
    var result = _argumentsToArray(arguments);

    switch (result.length) {
    case 0: result.push( 0.0 );
    case 1: result.push( 0.0 );
    case 2: result.push( 0.0 );
    case 3: result.push( 1.0 );
    }
    return result.splice( 0, 4 );
}

function create_buffer(gl, data, program, canvas, num_items, vertices, centers){
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
		(vertices.length*4) + (centers.length*4)
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

	/* Lighting Information */
	var lightPosition = hi(0, 0, 0, 1.0);
	var lightAmbient = hi(AmbR, AmbG, AmbB, 1.0);
	var lightDiffuse = hi(diff, diff, diff, 1.0);
	var lightSpecular = hi(spec, spec, spec, 1.0);

	var materialAmbient = hi(matAmbx, matAmbx, matAmbx, 1.0);
	var materialDiffuse = hi(matDif, matDif, matDif, 1.0);
	var materialSpecular = hi(matSpec, matSpec, matSpec, 1.0);
	var materialShininess = matShine;

	var ambientProduct = mult(lightAmbient, materialAmbient);
	var diffuseProduct = mult(lightDiffuse, materialDiffuse);
	var specularProduct = mult(lightSpecular, materialSpecular);
	
	var ambientVar = gl.getUniformLocation(program, 'ambientProduct');
	var diffuseVar = gl.getUniformLocation(program, 'diffuseProduct');
	var specularVar = gl.getUniformLocation(program, 'specularProduct');
	var positionVar = gl.getUniformLocation(program, 'lightPosition');
	var shininessVar = gl.getUniformLocation(program, 'shininess');

	gl.uniform4fv(ambientVar, flatten(ambientProduct));
    gl.uniform4fv(diffuseVar, flatten(diffuseProduct));
    gl.uniform4fv(specularVar, flatten(specularProduct));
    gl.uniform4fv(positionVar, flatten(lightPosition));

    gl.uniform1f(shininessVar, materialShininess);

	gl.uniformMatrix4fv(matProjectionVar, gl.FALSE, projMatrix);
	gl.uniformMatrix4fv(matViewVar, gl.FALSE, viewMatrix);

	myRotate(gl, num_items);
}

function myRotate(gl, num_items) {
	var loop = function(){
		old_time = new_time;
		new_time = performance.now();
		delta = new_time - old_time;
		
		if (is_rotating){
			theta += (direction * (delta / 500 / 6 * 2 *  Math.PI)); // miliseconds since window loaded
		}

		// send theta to the shader
		gl.uniform1f(theta_var, theta);
		render(gl, num_items);
		requestAnimationFrame(loop);
	};
	requestAnimationFrame(loop);
}

function render(gl, num_items){
	// draw to the screen
	gl.clear(gl.DEPTH_BUFFER_BIT | gl.COLOR_BUFFER_BIT);
	var offset = 0;
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

