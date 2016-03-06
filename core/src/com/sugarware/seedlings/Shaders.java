package com.sugarware.seedlings;

public class Shaders {
	
    public static String waterFragmentShader = 
    				"#ifdef GL_ES \n" +
    				"precision mediump float; \n" +
    				"#endif \n" +
    				"varying vec4 v_color; \n" +
    				"varying vec2 v_texCoords; \n" +
    				"uniform sampler2D u_texture; \n" +
    				"uniform sampler2D u_texture2; \n" +
    				"uniform float timedelta; \n" +
    				"uniform float ampdelta; \n" +
    				"void main()                                   \n" +
    				"{                                             \n" +
    				"  vec2 displacement = texture2D(u_texture2, v_texCoords/6.0).xy; \n" +
    				"  float t=v_texCoords.y +displacement.y*0.7-0.15+  (sin(v_texCoords.x * 40.0  + sin(ampdelta) * 0.75) * (0.115 * cos(ampdelta)));  \n" +
    				"  gl_FragColor = v_color * texture2D(u_texture, vec2(v_texCoords.x,t)); \n" +
    				"}";
    
    public static String water2FragmentShader =
    				"#ifdef GL_ES \n" +
    				"precision mediump float; \n" +
    				"#endif \n" +
    				"varying vec4 v_color; \n" +
    				"varying vec2 v_texCoords; \n" +
    				"uniform sampler2D u_texture; \n" +
    				"uniform sampler2D u_texture2; \n" +
    				"uniform float timedelta; \n" +
    				"uniform float ampdelta; \n" +
    				"void main()                                   \n" +
    				"{                                             \n" +
    				"  vec2 displacement = texture2D(u_texture2, v_texCoords/6.0).xy; \n" +
    				"  float t= v_texCoords.y + displacement.y *0.1-0.15+ (sin (v_texCoords.x * 60.0+timedelta) * 0.005);  \n" +
    				"  gl_FragColor = v_color * texture2D(u_texture, vec2(v_texCoords.x,t)); \n" +
    				"}"; 
    
    public static String vertexShader = 
    				"attribute vec4 a_position;     \n" +
    				"attribute vec2 a_texCoord0; \n" +
    				"uniform mat4 u_projTrans; \n" +
    				"varying vec4 v_color;varying vec2 v_texCoords;void main()                   \n" +
    				"{                             \n" +
    				"   v_color = vec4(1, 1, 1, 1);  \n" +
    				"   v_texCoords = a_texCoord0;  \n" +
    				"   gl_Position =  u_projTrans * a_position;   \n" +
    				"}";	
}



