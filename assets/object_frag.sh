precision mediump float;
varying vec4 vColor;
varying vec3 vPosition;
varying vec4 vAmbient;
varying vec4 vDiffuse;
varying vec4 vSpecular;
uniform sampler2D sTexture;
varying vec2 vTextureCoord;
varying float vFogFactor;
vec4 finalColor;

void main(){
	vec4 fogColor = vec4(0.97,0.76,0.03,1.0);
	//gl_FragColor = texture2D(sTexture,vTextureCoord);
	finalColor = texture2D(sTexture,vTextureCoord)*vFogFactor+fogColor*(1.0-vFogFactor);
	//finalColor = texture2D(sTexture,vTextureCoord);
	finalColor = finalColor * vDiffuse + finalColor * vAmbient + finalColor * vSpecular;
	//if(finalColor.a<2.0){
	//	discard;
	//}else{
	//	gl_FragColor = finalColor;
	gl_FragColor = finalColor * vDiffuse + finalColor * vAmbient + finalColor * vSpecular;
}