var layers;
const BASE = 1/3;
const SHIFT = 2/3;

function main(){
	var gl;
	layers = [];
	var canvas = document.getElementById('main_canvas');
	var program;
	var num_items = 0;
	var parent = [];
	var vertices = [];
	var depth = document.getElementById("depth").value; // starts to lag at depth = 5
	var is_base = true;

	gl = config(canvas);
	program = create_program(gl, canvas);
	get_verts(parent, depth, is_base);
	vertices = fill(vertices);
	num_items = vertices.length / 8;
	create_buffer(gl, vertices, program);
	render(gl, num_items);
}


function fill(vertices){
	for (i = 0; i < layers.length; i++){
		vertices = vertices.concat(layers[i]);
	}
	return vertices;
}


function create_buffer(gl, vertices, program){
	var buffer = gl.createBuffer(); // set aside memory on GPU for data
	gl.bindBuffer(gl.ARRAY_BUFFER, buffer); // select this buffer as something to manipulate
	gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW); //copy data to buffer

	var coordinatesVar = gl.getAttribLocation(program, "coordinates");
	gl.vertexAttribPointer(
		coordinatesVar, 
		2, 
		gl.FLOAT, 
		false, 
		0, 
		0);

	gl.enableVertexAttribArray(coordinatesVar);
}


function render(gl, num_items){
	// draw to the screen
	gl.clear(gl.COLOR_BUFFER_BIT);
	var offset = 0;
	for (i = 0; i < num_items; i++){
		gl.drawArrays(gl.TRIANGLE_FAN, offset, 4);
		offset += 4;
	}
}
	

function config(canvas){
	// Configure the canvas to use WebGl
	try {
		gl = canvas.getContext('webgl');
	} catch (e){
		try {
			gl = canvas.getContext('experimental-webgl');
		} catch (ex){
			throw new Error('no WebGL found');
		}
	}
	return gl;		
}


function create_program(gl, canvas){
	// Create simple vertex shader
	var vertCode = 
		'attribute vec4 coordinates;' + 
		'void main() {' +
		' gl_Position = coordinates;' +
		'}';

	var vertShader = gl.createShader(gl.VERTEX_SHADER);
	gl.shaderSource(vertShader, vertCode);
	gl.compileShader(vertShader);
	
	// check if everything compiled correctly
	if (!gl.getShaderParameter(vertShader, gl.COMPILE_STATUS)){
		throw new Error(gl.getShaderInfoLog(vertShader));
	}

	// Create simple fragment shader (R, G, B, alpha)
	var fragCode = 
		'precision mediump float;' +
		'void main() {' +
		'   gl_FragColor = vec4(0.0, 0.85, 0.49, 1.0);' +
		'}';
	
	var fragShader = gl.createShader(gl.FRAGMENT_SHADER);
	gl.shaderSource(fragShader, fragCode);
	gl.compileShader(fragShader);
	
	// check if everything compiled correctly
	if (!gl.getShaderParameter(fragShader, gl.COMPILE_STATUS)){
		throw new Error(gl.getShaderInfoLog(fragShader));
	}

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
	gl.clearColor(0.0, 0.0, 0.0, 1.0);
	gl.useProgram(program);
	return program;
}
