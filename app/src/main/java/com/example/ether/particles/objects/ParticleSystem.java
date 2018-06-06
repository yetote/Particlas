package com.example.ether.particles.objects;

import android.graphics.Color;
import android.util.Config;

import com.example.ether.particles.data.VertexArray;
import com.example.ether.particles.programs.ParticlesShaderProgram;
import com.example.ether.particles.utils.Geometry;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static com.example.ether.particles.Constants.BYTES_PER_FLOAT;

public class ParticleSystem {
    public static final int POSITION_COMPONENT_COUNT = 3;
    public static final int COLOR_COMPONENT_COUNT = 3;
    public static final int VECTOR_COMPONENT_COUNT = 3;
    public static final int PARTICLE_START_TIME_COMPONENT_COUNT = 1;

    public static final int TOTAL_COMPONENT_COUNT =
            POSITION_COMPONENT_COUNT
                    + COLOR_COMPONENT_COUNT
                    + VECTOR_COMPONENT_COUNT
                    + PARTICLE_START_TIME_COMPONENT_COUNT;

    public static final int STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT;


    private final float[] particles;
    private final VertexArray vertexArray;
    private final int maxParticleCount;

    private int currentParticleCount;
    private int nextParticle;

    public ParticleSystem(int maxParticleCount) {
        particles = new float[maxParticleCount * TOTAL_COMPONENT_COUNT];
        vertexArray = new VertexArray(particles);
        this.maxParticleCount = maxParticleCount;
    }

    public void addParticle(Geometry.Point position, int color, Geometry.Vector direction, float particleStartTime) {
        final int particleOffset = nextParticle * TOTAL_COMPONENT_COUNT;

        int currentOffset = particleOffset;

        nextParticle++;

        if (currentParticleCount < maxParticleCount) currentParticleCount++;

        if (nextParticle == maxParticleCount) nextParticle = 0;

        particles[currentOffset++] = position.x;
        particles[currentOffset++] = position.y;
        particles[currentOffset++] = position.z;

        particles[currentOffset++] = Color.red(color) / 255f;
        particles[currentOffset++] = Color.green(color) / 255f;
        particles[currentOffset++] = Color.blue(color) / 255f;

        particles[currentOffset++] = direction.x;
        particles[currentOffset++] = direction.y;
        particles[currentOffset++] = direction.z;

        particles[currentOffset++] = particleStartTime;

        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COMPONENT_COUNT);
    }

    public void bindData(ParticlesShaderProgram particlesShaderProgram) {
        int dataOffset = 0;
        vertexArray.setVertexAttribPointer(dataOffset, particlesShaderProgram.getPositionLocation(), POSITION_COMPONENT_COUNT, STRIDE);
        dataOffset += POSITION_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset, particlesShaderProgram.getColorLocation(), COLOR_COMPONENT_COUNT, STRIDE);
        dataOffset += COLOR_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset, particlesShaderProgram.getDirectionVectorLocation(), VECTOR_COMPONENT_COUNT, STRIDE);
        dataOffset += VECTOR_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,particlesShaderProgram.getParticlesStartTimeLocation(),PARTICLE_START_TIME_COMPONENT_COUNT,STRIDE);

    }
    public void draw(){
        glDrawArrays(GL_POINTS,0,currentParticleCount);
    }
}
