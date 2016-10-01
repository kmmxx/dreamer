uniform mat4 uMVPMatrix;
uniform mat4 uMMatrix;
attribute vec3 aPosition;
attribute vec4 aColor;
varying vec4 vColor;
varying vec3 vPosition;
varying vec4 vAmbient;
uniform vec3 uLightLocation;
uniform vec3 uLightDirection;
uniform vec3 uCamera;
attribute vec3 aNormal;
varying vec4 vDiffuse;
varying vec4 vSpecular;
varying vec2 vTextureCoord;
attribute vec2 aTextCoor;
varying float vFogFactor;

float computeFogFactor(int type){
	float tmpFactor = 0.8;
	//float fogDistance = length(uCamera-uMMatrix*vec4(aPosition,1)).xyz);
	float fogDistance = 2.0;
	const float end = 450.0;
	const float start = 350.0;
	if(type == 0){
		tmpFactor= max(min((end-fogDistance)/(end-start),1.0),0.0);
	}else if(type ==1){
		tmpFactor= 1.0-smoothstep(start,end,fogDistance);
	}
	return tmpFactor;
}

void pointLight(in vec3 normal,inout vec4 ambient,inout vec4 diffuse,inout vec4 specular,in vec3 lightLocation,in vec4 lightAmbient,in vec4 lightDiffuse,in vec4 lightSpecular){
	ambient = lightAmbient;
	vec3 normalTarget = aPosition + normal;
	vec3 newNormal = (uMMatrix * vec4(normalTarget,1)).xyz- (uMMatrix * vec4(aPosition,1)).xyz;
	newNormal = normalize(newNormal);
	vec3 eye = normalize(uCamera - (uMMatrix* vec4(aPosition,1)).xyz);
	vec3 vp = normalize(lightLocation- (uMMatrix*vec4(aPosition,1)).xyz);
	vp = normalize(vp);
	vec3 halfVector = normalize(vp+eye);
	float shininess = 50.0;
	float nDotViewPosition = max(0.0,dot(newNormal,vp));
	diffuse = lightDiffuse*nDotViewPosition;
	float nDotViewHalfVector = dot(newNormal,halfVector);
	float powerFactor = max(0.0,pow(nDotViewHalfVector,shininess));
	specular = lightSpecular * powerFactor;
}

//��λ����ռ���ķ���
void pointLightWithText(					//��λ����ռ���ķ���
  in vec3 normal,				//������
  inout vec4 ambient,			//����������ǿ��
  inout vec4 diffuse,				//ɢ�������ǿ��
  inout vec4 specular,			//���������ǿ��
  in vec3 lightLocation,			//��Դλ��
  in vec4 lightAmbient,			//������ǿ��
  in vec4 lightDiffuse,			//ɢ���ǿ��
  in vec4 lightSpecular			//�����ǿ��
){
  ambient=lightAmbient;			//ֱ�ӵó������������ǿ��  
  vec3 normalTarget=aPosition+normal;	//����任��ķ�����
  vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(aPosition,1)).xyz;
  newNormal=normalize(newNormal); 	//�Է��������
  //����ӱ���㵽�����������
  vec3 eye= normalize(uCamera-(uMMatrix*vec4(aPosition,1)).xyz);  
  //����ӱ���㵽��Դλ�õ�����vp
  vec3 vp= normalize(lightLocation-(uMMatrix*vec4(aPosition,1)).xyz);  
  vp=normalize(vp);//��ʽ��vp
  vec3 halfVector=normalize(vp+eye);	//����������ߵİ�����    
  float shininess=10.0;				//�ֲڶȣ�ԽСԽ�⻬
  float nDotViewPosition=max(0.0,dot(newNormal,vp)); 	//��������vp�ĵ����0�����ֵ
  diffuse=lightDiffuse*nDotViewPosition;				//����ɢ��������ǿ��
  float nDotViewHalfVector=dot(newNormal,halfVector);	//������������ĵ�� 
  float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess)); 	//���淴���ǿ������
  specular=lightSpecular*powerFactor;    			//���㾵��������ǿ��
}

void main(){
	gl_Position = uMVPMatrix * vec4(aPosition,1);
	vPosition = (uMMatrix * vec4(aPosition,1)).xyz;
	vec4 tmpDiffuse ,tmpAmbient,tmpSpecular;
	pointLightWithText(normalize(aNormal),tmpAmbient,tmpDiffuse,tmpSpecular,uLightLocation,vec4(0.5,0.5,0.5,1.0),vec4(0.5,0.5,0.5,1.0),vec4(0.3,0.3,0.3,1.0));
	//pointLightWithText(normalize(aNormal),tmpAmbient,tmpDiffuse,tmpSpecular,uLightLocation,vec4(1.0,1.0,1.0,1.0),vec4(1.0,1.0,1.0,1.0),vec4(1.0,1.0,1.0,1.0));
	vDiffuse = tmpDiffuse;
	vAmbient = tmpAmbient;
	vSpecular = tmpSpecular;
	vTextureCoord = aTextCoor;
	vFogFactor = computeFogFactor(0);
}