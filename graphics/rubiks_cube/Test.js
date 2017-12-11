var notes = "hi";

function test(text){
	this.name = "dom";
	this.notes = text;	
	this.teen = function(){
		console.log("I am a teen babe");
	}

	this.teen();
}