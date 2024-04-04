import java.nio.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL12C.*;
import static org.lwjgl.opengl.GL30C.*;

public class FBO {
	private int fboID;
	private int textureID;
	private int rboID;
	private int width;
	private int height;

	public FBO(int width, int height) {
		this.width = width;
		this.height = height;

		// Create the FBO
		fboID = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, fboID);

		// Create the texture
		textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureID);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_CLAMP_TO_EDGE);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureID, 0);

		// Create the RBO
		rboID = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, rboID);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, width, height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, rboID);

		// Check if the FBO is complete
		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			throw new RuntimeException("Framebuffer is not complete");
		}

		// Unbind the FBO
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

    public int getTextureID() {
        return textureID;
    }

	public void bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, fboID);
		glViewport(0, 0, width, height);
	}

	public void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(0, 0, width, height);
	}

	public void delete() {
		glDeleteFramebuffers(fboID);
		glDeleteTextures(textureID);
		glDeleteRenderbuffers(rboID);
	}
}