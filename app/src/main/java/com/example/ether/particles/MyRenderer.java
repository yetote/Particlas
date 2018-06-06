package com.example.ether.particles;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.example.ether.openglesdemo.R;
import com.example.ether.particles.objects.ParticleSystem;
import com.example.ether.particles.objects.ParticlesShooter;
import com.example.ether.particles.programs.ParticlesShaderProgram;
import com.example.ether.particles.utils.Geometry;
import com.example.ether.particles.utils.MatrixHelper;
import com.example.ether.particles.utils.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.glBlendColor;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.multiplyMV;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

public class MyRenderer implements GLSurfaceView.Renderer {
    private Context context;

    private final float[] projectMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectMatrix = new float[16];

    private ParticlesShaderProgram particlesProgram;
    private ParticleSystem particleSystem;
    private ParticlesShooter redParticlesShooter;
    private ParticlesShooter greenParticlesShooter;
    private ParticlesShooter blueParticlesShooter;
    private long globalStartTime;

    private int texture;

    public MyRenderer(Context context) {
        this.context = context;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);

        final float angleVarianceInDegrees = 5f;
        final float speedVariance = 1f;

        particlesProgram = new ParticlesShaderProgram(context);
        particleSystem = new ParticleSystem(10000);
        globalStartTime = System.nanoTime();

        final Geometry.Vector particlesDirection = new Geometry.Vector(0f, 0.5f, 0f);

        redParticlesShooter = new ParticlesShooter(new Geometry.Point(-1f, 0f, 0f), particlesDirection, Color.rgb(255, 50, 5), angleVarianceInDegrees, speedVariance);
        greenParticlesShooter = new ParticlesShooter(new Geometry.Point(0f, 0f, 0f), particlesDirection, Color.rgb(25, 255, 25), angleVarianceInDegrees, speedVariance);
        blueParticlesShooter = new ParticlesShooter(new Geometry.Point(1f, 0f, 0f), particlesDirection, Color.rgb(5, 50, 255), angleVarianceInDegrees, speedVariance);

        texture = TextureHelper.loadTexture(context, R.drawable.particle_texture);
    }

    /**
     * @param gl
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        MatrixHelper.perspectiveM(projectMatrix, 45, (float) width / (float) height, 1f, 10f);
        setIdentityM(viewMatrix, 0);
        translateM(viewMatrix, 0, 0f, -1.5f, -5f);
        multiplyMM(viewProjectMatrix, 0, projectMatrix, 0, viewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        float currentTime = (System.nanoTime() - globalStartTime) / 1000000000f;
        redParticlesShooter.addParticles(particleSystem, currentTime, 5);
        greenParticlesShooter.addParticles(particleSystem, currentTime, 5);
        blueParticlesShooter.addParticles(particleSystem, currentTime, 5);

        particlesProgram.useProgram();
        particlesProgram.setUniforms(viewProjectMatrix, currentTime, texture);
        particleSystem.bindData(particlesProgram);
        particleSystem.draw();
    }

}
