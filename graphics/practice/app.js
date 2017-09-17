var vertexShaderText = 
	'precision mediump float;' +
	'attribute vec2 vertPosition;' +
	'attribute vec3 vertColor;' +
	'varying vec3 fragColor;' +
	'void main() {' +
		'fragColor = vertColor;'+
		'gl_Position = vec4(vertPosition, 0.0, 1.0);'+
	'}';

var fragmentShaderText = 
	'precision mediump float;' +
	'varying vec3 fragColor;' +
	'void main() {' +
		'gl_FragColor = vec4(fragColor, 1.0);' +
	'}';
function get_verts(parent){
	pos = 1/3;
	neg = -1 * pos;
	parent.push(neg,neg,
				neg,pos,
				pos,pos,
				pos,neg);
	console.log("par size: " + parent.length);
}
var initDemo = function(){
	// want to clump information together to send to GPU
	// 3d cube (one corner is (-1,-1,-1) opposite corner is (1, 1, 1)
	// vertex shaders minimize communication between CPU and GPU
	var parent = [];
	get_verts(parent);
	console.log("PARENT: " + parent.length);
	var canvas = document.getElementById('surface');
	var gl = canvas.getContext('webgl');
	
	if (!gl){
		console.log("Webgl not supported, falling back on experimental webgl");
		gl = canvas.getContext('experimental-webgl');
	}
	if (!gl){
		alert("Your browser sucks");
	}

	//set background color (R,G,B,alpha)
	gl.clearColor(0.0, 0.0, 0.0, 1.0);
	// draw background to the screen
	gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT); 

	// create vertex and fragment (pixel shaders)
	var vertexShader = gl.createShader(gl.VERTEX_SHADER);
	var fragmentShader = gl.createShader(gl.FRAGMENT_SHADER);

	gl.shaderSource(vertexShader, vertexShaderText);
	gl.shaderSource(fragmentShader, fragmentShaderText);

	gl.compileShader(vertexShader);
	if (!gl.getShaderParameter(vertexShader, gl.COMPILE_STATUS)){
		console.error('ERROR complining vertex shader.', gl.getShaderInfoLog(vertexShader));
		return;
	}

	gl.compileShader(fragmentShader);
	if (!gl.getShaderParameter(fragmentShader, gl.COMPILE_STATUS)){
		console.error('ERROR compiling fragment shader.', gl.getShaderInfoLog(fragmentShader));
		return;
	}

	var program = gl.createProgram();
	gl.attachShader(program, vertexShader);
	gl.attachShader(program, fragmentShader);
	gl.linkProgram(program);
	if (!gl.getProgramParameter(program, gl.LINK_STATUS)){
		console.error('ERROR linking program.', gl.getProgramInfoLog(program));
		return;
	}
	
	gl.validateProgram(program);
	if(!gl.getProgramParameter(program, gl.VALIDATE_STATUS)){
		console.error('ERROR validating program!', gl.getProgramInfoLog(program));
		return;
	}
	
	// Create buffer
	var triangleVertices = [
		//X, Y		R,G,B
		-0.5, 0.5,	1.0, 1.0, 0.0,
		-0.5, -0.5,	0.7, 0.0, 1.0,
		0.5, -0.5,	0.1, 1.0, 0.6
	];
	
	var triangleVertexBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, triangleVertexBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(triangleVertices), gl.STATIC_DRAW);
	
	var positionAttribLocation = gl.getAttribLocation(program, 'vertPosition');
	var colorAttribLocation = gl.getAttribLocation(program, 'vertColor');
	gl.vertexAttribPointer(
		positionAttribLocation,
		2, // elements per attribute
		gl.FLOAT, // type of elements
		gl.FALSE, // normalized data
		5 * Float32Array.BYTES_PER_ELEMENT, // size of individual vertex
		0, // offset from beginning of a single vertex to this attribute
	);
	
	gl.vertexAttribPointer(
		colorAttribLocation,
		3,
		gl.FLOAT,
		gl.FALSE,
		5 * Float32Array.BYTES_PER_ELEMENT,
		2 * Float32Array.BYTES_PER_ELEMENT
	);

	gl.enableVertexAttribArray(positionAttribLocation);
	gl.enableVertexAttribArray(colorAttribLocation);

	// main render loop
	gl.useProgram(program);
	gl.drawArrays(gl.TRIANGLES, 0, 3);
	
};

// when dividing squares:
// center of each square should be halfway between square and edge of canvas

