var layers = [];
var BASE = 0;
const SHIFT = 2/3;
var direction = 1;
var oldTime = 0;
var newTime = 0;
var delta = 0;
var rotating = false;

window.onload = function main(){
	run();
}

function run(){
	var canvas = document.getElementById('web_gl_canvas');
	var depth = document.getElementById('depth');

	var gl = init(canvas);
	canvas.onclick = changeDirection;

	var vertShader = loadVertShader(gl);
	var fragShader = loadFragShader(gl);
	var program = link(gl, canvas, vertShader, fragShader);
	gl.useProgram(program);

	BASE = 1/3;
	layers = [];
	parent = [];
	getVerts(parent, depth, true);
	var vertices;
	vertices = fill(vertices);

	BASE = 0;
	layers = [];
	parent = [];
	get_verts(parent, depth, true);
	var centers;
	centers = fill(centers);

	var count = vertices.length / 8;

	var normals = [
      0.0, 0.0,
      0.0, 0.0,
      0.0 ,0.0,
      0.0 ,0.0
   ];

   for (i = 1; i < count; i++){
   		normals = normals.concat(normals);
   }

   createBuffer(gl, program, count, vertices, centers, normals);

}

function fill(arr){
	for (i = 0; i < layers.length; i++){
		arr = arr.concat(layers[i]);
	}
	return arr;
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

function loadVertShader(gl){
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

function loadFragShader(gl){
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

function link(gl, canvas, vertShader, fragShader){
	// Put vertex and fragment shaders together into complete program
	var program = gl.createProgram();
	gl.attachShader(program, vertShader);
	gl.attachShader(program, fragShader);
	gl.linkProgram(program);
	
	// make sure program was created correctly
	if (!gl.getProgramParameter(program, gl.LINK_STATUS)){
		throw new Error(gl.getProgramInfoLog(program));
	}

	gl.viewport(0, 0, canvas.width, canvas.height);
	gl.clearColor(0.65, 0.7, 0.65, 1.0);
	return program;
}

function createBuffer(gl, program, count, vertices, centers, normals){
	var vertBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, vertBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, vertices, gl.STATIC_DRAW);

	var coordLocation = gl.getAttribLocation(program, "coordinates");
	gl.vertexAttribPointer(coordLocation, 2, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(coordLocation);

	var centerBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, centerBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, centers, gl.STATIC_DRAW);

	var centerLocation = gl.getAttribLocation(program, "center");
	gl.vertexAttribPointer(centerLocation, 2, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(centerLocation);

	var normalsBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, normalsBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, normals, gl.STATIC_DRAW);

	var normalsLocation = gl.getAttribLocation(program, "vertNormal");
	gl.vertexAttribPointer(normalsLocation, 2, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(normalsLocation);

	var thetaLoc = gl.getUniformLocation(program, "theta");

   	var projection = ortho(-1, 1, -1, 1, -100, 100);

	var lightPosition = vec4(1.0, 1.0, 1.0, 0.0 );
	var lightAmbient = vec4(0.2, 0.2, 0.2, 1.0 );
	var lightDiffuse = vec4( 1.0, 1.0, 1.0, 1.0 );
	var lightSpecular = vec4( 1.0, 1.0, 1.0, 1.0 );

	var materialAmbient = vec4( 1.0, 0.0, 1.0, 1.0 );
	var materialDiffuse = vec4( 1.0, 0.8, 0.0, 1.0);
	var materialSpecular = vec4( 1.0, 0.8, 0.0, 1.0 );
	var materialShininess = 100.0;

  	var ambientProduct = mult(lightAmbient, materialAmbient);
    var diffuseProduct = mult(lightDiffuse, materialDiffuse);
    var specularProduct = mult(lightSpecular, materialSpecular);

    gl.uniform4fv(gl.getUniformLocation(program, "ambientProduct"), flatten(ambientProduct));
    gl.uniform4fv(gl.getUniformLocation(program, "diffuseProduct"), flatten(diffuseProduct));
    gl.uniform4fv(gl.getUniformLocation(program, "specularProduct"), flatten(specularProduct));
    gl.uniform4fv(gl.getUniformLocation(program, "lightPosition"), flatten(lightPosition));

    gl.uniform1f(gl.getUniformLocation(program, "shininess"),materialShininess);

    gl.uniformMatrix4fv( gl.getUniformLocation(program, "projectionMatrix"), false, flatten(projection));

    rotate(gl, count, thetaLoc);
}

function rotate(gl, count, thetaLoc) {
	var loop = function(){
		oldTime = newTime;
		newTime = performance.now();
		delta = newTime - oldTime;
		
		if (rotating){
			theta += (direction * (delta / 500 / 6 * 2 *  Math.PI)); // miliseconds since window loaded
		}

		// send theta to the shader
		gl.uniform1f(thetaLoc, theta);
		render(gl, count);
		requestAnimationFrame(loop);
	};
	requestAnimationFrame(loop);
}

function render(gl, count){
	// draw to the screen
	gl.clear(gl.DEPTH_BUFFER_BIT | gl.COLOR_BUFFER_BIT);
	var offset = 0;

	for (i = 0; i < count; i++){
		gl.drawArrays(gl.TRIANGLE_FAN, offset, 4);
		offset += 4;
	}
}

function changeRotation(){
	if (rotating == false){
		rotating = true;
	} else {
		rotating = false;
	}
}

function changeDirection(){
	direction = direction * -1;
}

function getVerts(parent, depth, is_base){
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
	getVerts(children, depth, false);
	return;
}