uniform mat4 uMVPMatrix;
uniform mat4 uMMatrix;
attribute vec3 aPosition;
attribute vec4 aColor;
attribute vec4 vColor;
varying vec2 vTextureCoord;
attribute vec2 aTextCoor;

void main(){
	gl_Position = uMVPMatrix * vec4(aPosition,1);
	vTextureCoord = aTextCoor;
}