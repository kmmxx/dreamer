precision mediump float;
varying vec4 vColor;
varying vec3 vPosition;
varying vec4 vAmbient;
varying vec4 vDiffuse;
varying vec4 vSpecular;
uniform sampler2D sTexture;
uniform sampler2D sGrassTexture;
uniform sampler2D sRockTexture;
uniform float landStartY;
uniform float landYSpan;
varying vec2 vTextureCoord;
varying float vFogFactor;
vec4 finalColor;
varying float currY;

void main(){
	vec4 fogColor = vec4(0.97,0.76,0.03,1.0);
	vec4 gColor = texture2D(sGrassTexture,vTextureCoord);
	vec4 rColor = texture2D(sRockTexture,vTextureCoord);
	if(currY<landStartY){
		finalColor = gColor;
	}else if(currY>landStartY + landYSpan){
		finalColor = rColor;
	}else{
		float ratio = (currY - landStartY)/landYSpan;
		finalColor = ratio * rColor + (1.0-ratio)* gColor;
	}
	//gl_FragColor = texture2D(sTexture,vTextureCoord);
	//finalColor = texture2D(sTexture,vTextureCoord)*vFogFactor+fogColor*(1.0-vFogFactor);
	//finalColor = texture2D(sTexture,vTextureCoord);
	//finalColor = finalColor * vDiffuse + finalColor * vAmbient + finalColor * vSpecular;
	gl_FragColor = finalColor * vDiffuse + finalColor * vAmbient + finalColor * vSpecular;
}