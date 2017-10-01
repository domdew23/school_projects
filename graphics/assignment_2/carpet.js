var layers = [];
const BASE = 1/3;
const SHIFT = 2/3;
var direction = 1;
var rotate = false;
var theta = 0;
var new_time = 0;
var old_time = 0;
var time = 0;

// translation matrix: move in space
// rotation matrix: spin around
// scale matrix: make bigger or smaller
// projection matrix: apply 3d space to 2d space

window.onload = function main(){
	run();
}

function run(){
	var num_items = 0;
	var parent = [];
	var vertices = [];
	var is_base = true;
	layers = [];
	var canvas = document.getElementById('web_gl_canvas');
	var depth = document.getElementById('depth').value;
	
	var gl = init(canvas);
	canvas.onclick = change_direction;

	var vert_shader = load_vertex_shader(gl);
	var frag_shader = load_frag_shader(gl);
	var program = link_program(gl, canvas, vert_shader, frag_shader);

	get_verts(parent, depth, is_base);
	vertices = fill(vertices);
	num_items = vertices.length / 8;
	create_buffer(gl, vertices, program, canvas, num_items, program);
}

function change_direction(){
	direction = direction * -1;
}

function change_rotation(){
	if (rotate == false){
		rotate = true;
	} else {
		rotate = false;
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
	gl.clearColor(0.0, 0.0, 0.0, 1.0);
	return program;
}

function create_buffer(gl, vertices, program, canvas, num_items, program){
	gl.useProgram(program);
	var buffer = gl.createBuffer(); // set aside memory on GPU for data
	gl.bindBuffer(gl.ARRAY_BUFFER, buffer); // select this buffer as something to manipulate
	gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW); //copy data to buffer

	var coordinates_var = gl.getAttribLocation(program, "coordinates");
	gl.vertexAttribPointer(
		coordinates_var, 
		2, 
		gl.FLOAT, 
		false, 
		0, 
		0);

	gl.enableVertexAttribArray(coordinates_var);

	var matWorldUniformLocation = gl.getUniformLocation(program, 'mWorld');
	var matViewUniformLocation = gl.getUniformLocation(program, 'mView');
	var matProjUniformLocation = gl.getUniformLocation(program, 'mProj');

	var worldMatrix = new Float32Array(16);
	var viewMatrix = new Float32Array(16);
	var projMatrix = new Float32Array(16);
	mat4.identity(worldMatrix);
	mat4.lookAt(viewMatrix, [0, 0, -3], [0,0,0], [0,1,0]);
	mat4.perspective(projMatrix, glMatrix.toRadian(45), canvas.width/canvas.height, 0.1, 1000.0);

	// send to shader
	gl.uniformMatrix4fv(matWorldUniformLocation, gl.FALSE, worldMatrix);
	gl.uniformMatrix4fv(matViewUniformLocation, gl.FALSE, viewMatrix);
	gl.uniformMatrix4fv(matProjUniformLocation, gl.FALSE, projMatrix);

	var identityMatrix = new Float32Array(16);
	mat4.identity(identityMatrix);
		
	move(gl, worldMatrix, identityMatrix, matWorldUniformLocation, num_items);

}

function move(gl, worldMatrix, identityMatrix, matWorldUniformLocation, num_items) {
	var loop = function(){
		old_time = new_time;
		new_time = performance.now();
		time = new_time - old_time;
		if (rotate){
			theta += direction * (time / 1000 / 6 * 2 *  Math.PI); // miliseconds since window loaded
		}
		mat4.rotate(worldMatrix, identityMatrix, theta, [0,0,1]);
		gl.uniformMatrix4fv(matWorldUniformLocation, gl.FALSE, worldMatrix);
		render(gl, num_items);
		requestAnimationFrame(loop);
	};
	requestAnimationFrame(loop);
}

function render(gl, num_items){
	// draw to the screen
	gl.clear(gl.COLOR_BUFFER_BIT);
	var offset = 0;
	for (i = 0; i < num_items; i++){
		//console.log(num_items + " " + offset);
		gl.drawArrays(gl.TRIANGLE_FAN, offset, 4);
		offset += 4;
	}
}

function fill(vertices){
	for (i = 0; i < layers.length; i++){
		vertices = vertices.concat(layers[i]);
	}
	return vertices;
}

function get_verts(parent, depth, is_base=false){
	if (is_base){
		var base = [
			//X  Y	
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
						// bottom_left
						vert_to_add = vert - SHIFT;
						child.push(vert_to_add);
						break;
					case 1:
						// side left
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
			layers.push(child);
		}
	}

	depth--;
	get_verts(children, depth, false);
	return;
}