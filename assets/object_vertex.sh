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

//定位光光照计算的方法
void pointLightWithText(					//定位光光照计算的方法
  in vec3 normal,				//法向量
  inout vec4 ambient,			//环境光最终强度
  inout vec4 diffuse,				//散射光最终强度
  inout vec4 specular,			//镜面光最终强度
  in vec3 lightLocation,			//光源位置
  in vec4 lightAmbient,			//环境光强度
  in vec4 lightDiffuse,			//散射光强度
  in vec4 lightSpecular			//镜面光强度
){
  ambient=lightAmbient;			//直接得出环境光的最终强度  
  vec3 normalTarget=aPosition+normal;	//计算变换后的法向量
  vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(aPosition,1)).xyz;
  newNormal=normalize(newNormal); 	//对法向量规格化
  //计算从表面点到摄像机的向量
  vec3 eye= normalize(uCamera-(uMMatrix*vec4(aPosition,1)).xyz);  
  //计算从表面点到光源位置的向量vp
  vec3 vp= normalize(lightLocation-(uMMatrix*vec4(aPosition,1)).xyz);  
  vp=normalize(vp);//格式化vp
  vec3 halfVector=normalize(vp+eye);	//求视线与光线的半向量    
  float shininess=10.0;				//粗糙度，越小越光滑
  float nDotViewPosition=max(0.0,dot(newNormal,vp)); 	//求法向量与vp的点积与0的最大值
  diffuse=lightDiffuse*nDotViewPosition;				//计算散射光的最终强度
  float nDotViewHalfVector=dot(newNormal,halfVector);	//法线与半向量的点积 
  float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess)); 	//镜面反射光强度因子
  specular=lightSpecular*powerFactor;    			//计算镜面光的最终强度
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