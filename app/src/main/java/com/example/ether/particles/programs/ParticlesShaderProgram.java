package com.example.ether.particles.programs;

import android.content.Context;

import com.example.ether.openglesdemo.R;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;

public class ParticlesShaderProgram extends ShaderProgram {
    public final int uMatrixLocation;
    public final int uTimeLocation;

    public final int aDirectionVectorLocation;
    public final int aParticlesStartTimeLocation;
    public final int aPositionLocation;
    public final int aColorLocation;

    private final int uTextureUnitLocation;
    public ParticlesShaderProgram(Context context) {
        super(context, R.raw.particles_vertex_shader, R.raw.particles_fragment_shader);

        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTimeLocation = glGetUniformLocation(program, U_TIME);

        aColorLocation = glGetAttribLocation(program, A_COLOR);
        aDirectionVectorLocation = glGetAttribLocation(program, A_DIRECTION_VECTOR);
        aParticlesStartTimeLocation = glGetAttribLocation(program, A_PARTICLES_START_TIME);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);

        uTextureUnitLocation=glGetUniformLocation(program,U_TEXTURE_UNIT);
    }
    public void setUniforms(float[] matrix,float elapsedTime,int textureId){
        glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
        glUniform1f(uTimeLocation,elapsedTime);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D,textureId);
        glUniform1i(uTextureUnitLocation,0);
    }

    public int getDirectionVectorLocation() {
        return aDirectionVectorLocation;
    }

    public int getParticlesStartTimeLocation() {
        return aParticlesStartTimeLocation;
    }

    public int getPositionLocation() {
        return aPositionLocation;
    }

    public int getColorLocation() {
        return aColorLocation;
    }
}
