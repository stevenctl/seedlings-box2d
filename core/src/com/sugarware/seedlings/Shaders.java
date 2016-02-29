/*
 * Decompiled with CFR 0_110.
 */
package com.sugarware.seedlings;

public class Shaders {
    public static String waterFragmentShader = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec4 v_color;\nvarying vec2 v_texCoords;\nuniform sampler2D u_texture;\nuniform sampler2D u_texture2;\nuniform float timedelta;\nuniform float ampdelta;\nvoid main()                                  \n{                                            \n  vec2 displacement = texture2D(u_texture2, v_texCoords/6.0).xy;\n  float t=v_texCoords.y +displacement.y*0.7-0.15+  (sin(v_texCoords.x * 40.0  + sin(ampdelta) * 0.75) * (0.115 * cos(ampdelta))); \n  gl_FragColor = v_color * texture2D(u_texture, vec2(v_texCoords.x,t));\n}";
    public static String water2FragmentShader = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec4 v_color;\nvarying vec2 v_texCoords;\nuniform sampler2D u_texture;\nuniform sampler2D u_texture2;\nuniform float timedelta;\nuniform float ampdelta;\nvoid main()                                  \n{                                            \n  vec2 displacement = texture2D(u_texture2, v_texCoords/6.0).xy;\n  float t= v_texCoords.y + displacement.y *0.1-0.15+ (sin (v_texCoords.x * 60.0+timedelta) * 0.005); \n  gl_FragColor = v_color * texture2D(u_texture, vec2(v_texCoords.x,t));\n}";
    public static String vertexShader = "attribute vec4 a_position;    \nattribute vec2 a_texCoord0;\nuniform mat4 u_projTrans;\nvarying vec4 v_color;varying vec2 v_texCoords;void main()                  \n{                            \n   v_color = vec4(1, 1, 1, 1); \n   v_texCoords = a_texCoord0; \n   gl_Position =  u_projTrans * a_position;  \n}";
    public static String ambientShader = "varying LOWP vec4 vColor; \nvarying vec2 vTexCoord; \nuniform sampler2D u_texture; \nuniform LOWP vec4 ambientColor; \nvoid main() { \n    vec4 diffuseColor = texture2D(u_texture, vTexCoord); \n    vec3 ambient = ambientColor.rgb * ambientColor.a; \n    vec3 final = vColor * diffuseColor.rgb * ambient; \n    gl_FragColor = vec4(final, diffuseColor.a); \n    }";
    public static String lightShader = "varying LOWP vec4 vColor;\nvarying vec2 vTexCoord;\n\n//our texture samplers\nuniform sampler2D u_texture; //diffuse map\nuniform sampler2D u_lightmap;   //light map\n\n//resolution of screen\nuniform vec2 resolution; \n \nvoid main() {\n    vec2 lighCoord = (gl_FragCoord.xy / resolution.xy);\n    vec4 Light = texture2D(u_lightmap, lighCoord);\n \n    gl_FragColor = vColor * Light;\n}\n";
}

