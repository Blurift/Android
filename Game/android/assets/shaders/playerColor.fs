precision highp float;

varying vec3 n;
varying vec2 uv;

uniform sampler2D tex;
uniform vec3 r_color;

//#pragma include "noise2D.glsl // for snoise(vec2 v)
//#pragma include "noise3D.glsl" //  for snoise(vec3 v)
//#pragma include "noise4D.glsl" //  for snoise(vec4 v)
//#pragma include "cellular2D.glsl" //  for cellular(vec2 P)
//#pragma include "cellular2x2.glsl" //  for cellular2x2(vec2 P)
//#pragma include "cellular2x2x2.glsl" //  for cellular2x2x2(vec3 P)
//#pragma include "cellular3D.glsl" //  cellular(vec3 P)


void main(void)
{
	vec3 color = texture2D(tex, uv).rgb;
	float gray = (color.r + color.g + color.b) / 3.0;
	vec3 grayscale = vec3(gray) * r_color;
	vec3 c_filt[12];
    c_filt[0] = vec3(32/255,40/255,32/255);
    c_filt[1] = vec3(100/255,55/255,38/255);
    c_filt[2] = vec3(146/255,94/255,53/255);
    c_filt[3] = vec3(116/255,67/255,46/255);
    c_filt[4] = vec3(112/255,65/255,25/255);
    c_filt[5] = vec3(49/255,89/255,79/255);
    c_filt[6] = vec3(237/255,251/255,223/255);
    c_filt[7] = vec3(220/255,152/255,116/255);
    c_filt[8] = vec3(158/255,104/255,50/255);
    c_filt[9] = vec3(52/255,40/255,154/255);
    c_filt[10] = vec3(64/255,50/255,169/255);
    c_filt[11] = vec3(28/255,18/255,104/255);
    float epsilon = 0.35;
    for(int i = 0;i < 12;i++){
    	vec3 dif = abs(color - c_filt[i]);
    	if (dif.r > epsilon && dif.g > epsilon && dif.b > epsilon){
    		grayscale = color;
    		break;
    	}
    }

	gl_FragColor = vec4(grayscale, 1.0);
}
