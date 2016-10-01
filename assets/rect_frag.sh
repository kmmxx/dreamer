precision mediump float;
varying vec4 vColor;
uniform float alpha;
varying vec3 vPosition;
varying vec4 vAmbient;
varying vec4 vDiffuse;
varying vec4 vSpecular;
uniform sampler2D sTexture;
varying vec2 vTextureCoord;

void main(){
	gl_FragColor = texture2D(sTexture,vTextureCoord)*alpha;
}