precision mediump float;
varying  vec3 vPosition;  //����λ��
varying vec4 vColor;
void main() {
	vec4 finalColor=vec4(0.8,0.8,0.8,1.0);
     //gl_FragColor = vColor;//����ƬԪ��ɫֵ
      gl_FragColor = finalColor;//����ƬԪ��ɫֵ
} 