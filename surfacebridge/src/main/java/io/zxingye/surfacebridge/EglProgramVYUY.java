package io.zxingye.surfacebridge;

import android.opengl.GLES20;

public class EglProgramVYUY extends EglProgramYUV {

    private final int offsetLoc;

    public EglProgramVYUY(EglBufferObjectHolder eglBOHolder) {
        super(FRAGMENT_SHADER_RGB_TO_VYUY, eglBOHolder);
        offsetLoc = GLES20.glGetUniformLocation(programId, "u_Offset");
    }

    @Override
    protected int getRealViewportWidth(int width) {
        return width / 2;
    }

    @Override
    protected int getRealViewportHeight(int height) {
        return height;
    }

    @Override
    protected void onDraw(int width, int height) {
        GLES20.glUniform1f(offsetLoc, 1.f / (float) width);
        super.onDraw(width, height);
    }

    @Override
    public FrameFormat getFrameFormat() {
        return FrameFormat.VYUY;
    }

    private static final String FRAGMENT_SHADER_RGB_TO_VYUY = "" +
            "#version 300 es\n" +
            "#extension GL_OES_EGL_image_external_essl3 : require\n" +
            "precision highp float;\n" +
            "in vec2 v_texCoord;\n" +
            "layout(location = 0) out vec4 outColor;\n" +
            "uniform samplerExternalOES s_TextureMap;\n" +
            "uniform float u_Offset;\n" +
            "uniform vec3 COEF_Y;\n" +
            "uniform vec3 COEF_U;\n" +
            "uniform vec3 COEF_V;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    vec2 texelOffset = vec2(u_Offset, 0.0);\n" +
            "    vec4 color0 = texture(s_TextureMap, v_texCoord);\n" +
            "    vec4 color1 = texture(s_TextureMap, v_texCoord + texelOffset);\n" +
            "    float y0 = dot(color0.rgb, COEF_Y);\n" +
            "    float u0 = dot(color0.rgb, COEF_U) + 0.5;\n" +
            "    float v0 = dot(color0.rgb, COEF_V) + 0.5;\n" +
            "    float y1 = dot(color1.rgb, COEF_Y);\n" +
            "    outColor = vec4(v0, y0, u0, y1);\n" +
            "}";
}
