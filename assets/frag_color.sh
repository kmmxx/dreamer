precision mediump float;
varying  vec3 vPosition;  //顶点位置
varying vec4 vColor;
void main() {
	vec4 finalColor=vec4(0.8,0.8,0.8,1.0);
     //gl_FragColor = vColor;//给此片元颜色值
      gl_FragColor = finalColor;//给此片元颜色值
} 