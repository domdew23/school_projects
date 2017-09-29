var gl;

function main(){
	var canvas = document.getElementById('web_gl_canvas');
	var depth = document.getElementById('depth');
	init(canvas);
	var vert_shader = load_vertex_shader();
	var frag_shaer = load_frag_shader();
	var program = link_program(canvas, vert_shader, frag_shaer);
	var vertices = [
		-.33,-.33,
		-.33,.33,
		.33,.33,
		.33, -.33];

	create_buffer(vertices, program);
	render();
}

function init(canvas){	
	try {
		gl = canvas.getContext('webgl');
	} catch (e1) {
		try {
			gl = canvas.getContext('experimental-webl');
		} catch (e2){
			throw new Error('no Webgl found.');
		}
	}
}

function load_vertex_shader(){
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

function load_frag_shader(){
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

function link_program(canvas, vert_shader, frag_shader){
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
	gl.useProgram(program);
	return program;
}

function create_buffer(vertices, program){
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
}

function render(){
	gl.clear(gl.COLOR_BUFFER_BIT);
	gl.drawArrays(gl.TRIANGLE_FAN, 0, 4);
}