package view.view3D.GL3;

import java.io.BufferedReader;
import java.io.FileReader;

import model.math.Decimal;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;

import view.scene.PrintableScene;

public class SceneViewUpdate extends ViewUpdate {

	
	// Data for drawing Axis
	float verticesAxis[] = { -20.0f, 0.0f, 0.0f, 1.0f, 20.0f, 0.0f, 0.0f, 1.0f,

	0.0f, -20.0f, 0.0f, 1.0f, 0.0f, 20.0f, 0.0f, 1.0f,

	0.0f, 0.0f, -20.0f, 1.0f, 0.0f, 0.0f, 20.0f, 1.0f };

	float colorAxis[] = { 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
	1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
	0.0f, 0.0f, 1.0f, 0.0f };

	int[] vao;
	
	// scene à afficher
	private PrintableScene scene;
	
	// Liste des object affichables
	private ViewScene view;

	private int program;
	
	private String vertexFileName;
	private String fragmentFileName;

	private int projMatrixLoc;
	private int viewMatrixLoc;
	
	private Matrix projMatrix, viewMatrix;
	
	private boolean updated;

	
	public SceneViewUpdate(String inc) {
		super(inc);
		
		view = new ViewScene();
		projMatrix = new Matrix();
		viewMatrix = new Matrix();
		
		vertexFileName = "data/color.vert";
		fragmentFileName = "data/color.frag";

		updated = true;
	}
	
	public void setScene (PrintableScene sc) {
		scene = sc;
		if (scene != null) {
			view.computeScene(scene);			
		}
		updated = true;
	}
	
	/** GL Init */
	@Override
	public void init(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		gl.glEnable(GL3.GL_DEPTH_TEST);
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

		program = setupShaders(gl);
		view.generateBuffer(gl);
		updated = false;
//		testingBuffers(gl);

		
		// Set the camera at the good place
		camera.setPosition(new Decimal(10), new Decimal(2), new Decimal(10));
		camera.setDirection (new Decimal(0), new Decimal(2), new Decimal(-5));
		camera.setHaut(new Decimal(0), new Decimal(1), new Decimal(0));
		
	}

	private void testingBuffers(GL3 gl) {
		int buffers[] = new int[2];
		vao = new int[1];
		gl.glGenVertexArrays(3, vao, 0);
		gl.glBindVertexArray(vao[0]);
		// Generate two slots for the vertex and color buffers
		gl.glGenBuffers(2, buffers, 0);
		// bind buffer for vertices and copy data into buffer
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers[0]);
		gl.glBufferData(GL3.GL_ARRAY_BUFFER, verticesAxis.length * Float.SIZE / 8,
		Buffers.newDirectFloatBuffer(verticesAxis), GL3.GL_STATIC_DRAW);
		gl.glEnableVertexAttribArray(this.view.vertexLoc);
		gl.glVertexAttribPointer(this.view.vertexLoc, 4, GL3.GL_FLOAT, false, 0, 0);

		// bind buffer for colors and copy data into buffer
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers[1]);
		gl.glBufferData(GL3.GL_ARRAY_BUFFER, colorAxis.length * Float.SIZE / 8,
		Buffers.newDirectFloatBuffer(colorAxis), GL3.GL_STATIC_DRAW);
		gl.glEnableVertexAttribArray(this.view.colorLoc);
		gl.glVertexAttribPointer(this.view.colorLoc, 4, GL3.GL_FLOAT, false, 0, 0);
	}

	/** GL Render loop */
	@Override
	public void display(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		if (updated) {
			view.generateBuffer(gl);
			updated = false;
		}

		renderScene(gl);
	}

	/** GL Window Reshape */
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL3 gl = drawable.getGL().getGL3();
		changeSize(gl, width, height);
	}

	/** GL Complete */
	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	
	private void renderScene(GL3 gl) {

		gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);

		// adapter la camera
		this.viewMatrix = camera.getMatrix();

		gl.glUseProgram(program);
		setUniforms(gl);

/*		gl.glBindVertexArray(vao[0]);
		gl.glDrawArrays(GL3.GL_LINES, 0, 6);
*/

		view.dispayObjects(gl);
	}


	void setUniforms(GL3 gl) {
		// must be called after glUseProgram
		gl.glUniformMatrix4fv(projMatrixLoc, 1, false, projMatrix.data, 0);
		gl.glUniformMatrix4fv(viewMatrixLoc, 1, false, viewMatrix.data, 0);
	}

	
	int setupShaders(GL3 gl) {

		String vs = null;
		String fs = null;

		int p, v, f;

		v = gl.glCreateShader(GL3.GL_VERTEX_SHADER);
		f = gl.glCreateShader(GL3.GL_FRAGMENT_SHADER);

		vs = textFileRead(vertexFileName);
		fs = textFileRead(fragmentFileName);

		String vv = vs;
		String ff = fs;

		gl.glShaderSource(v, 1, new String[] { vv }, null);
		gl.glShaderSource(f, 1, new String[] { ff }, null);

		gl.glCompileShader(v);
		gl.glCompileShader(f);

		printShaderInfoLog(gl, v);
		printShaderInfoLog(gl, f);

		p = gl.glCreateProgram();
		gl.glAttachShader(p, v);
		gl.glAttachShader(p, f);

		gl.glBindFragDataLocation(p, 0, "outputF");
		gl.glLinkProgram(p);
		printProgramInfoLog(gl, p);

		view.vertexLoc = gl.glGetAttribLocation(p, "position");
		view.colorLoc = gl.glGetAttribLocation(p, "color"); 

		projMatrixLoc = gl.glGetUniformLocation(p, "projMatrix");
		viewMatrixLoc = gl.glGetUniformLocation(p, "viewMatrix");

		return (p);
	}

	
	public String textFileRead(String filePath) {
		// Read the data in
		BufferedReader reader = null;
		try {
			// Read in the source
			reader = new BufferedReader(new FileReader(filePath));
			final StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null)
			sb.append(line).append("\n");
			final String text = sb.toString();
	
			return text;

		} catch (final Exception ex) {
			ex.printStackTrace();
			return "";
		} finally {
			try {
					reader.close();
			} catch (final Exception ex) {}
		}
	}

	/** Gets a program parameter value */
	public int getProgramParameter(GL3 gl, int obj, int paramName) {
		final int params[] = new int[1];
		gl.glGetProgramiv(obj, paramName, params, 0);
		return params[0];
	}

		/** Retrieves the info log for the shader */
	public String printShaderInfoLog(GL3 gl, int obj) {
		// Otherwise, we'll get the GL info log
		final int logLen = getShaderParameter(gl, obj, GL3.GL_INFO_LOG_LENGTH);
		if (logLen <= 0) return "";

		// Get the log
		final int[] retLength = new int[1];
		final byte[] bytes = new byte[logLen + 1];
		gl.glGetShaderInfoLog(obj, logLen, retLength, 0, bytes, 0);
		final String logMessage = new String(bytes);

		return String.format("ShaderLog: %s", logMessage);
	}

		/** Get a shader parameter value. See 'glGetShaderiv' */
	private int getShaderParameter(GL3 gl, int obj, int paramName) {
		final int params[] = new int[1];
		gl.glGetShaderiv(obj, paramName, params, 0);
		return params[0];
	}

		/** Retrieves the info log for the program */
	public String printProgramInfoLog(GL3 gl, int obj) {
		// get the GL info log
		final int logLen = getProgramParameter(gl, obj, GL3.GL_INFO_LOG_LENGTH);
		if (logLen <= 0) return "";

		// Get the log
		final int[] retLength = new int[1];
		final byte[] bytes = new byte[logLen + 1];
		gl.glGetProgramInfoLog(obj, logLen, retLength, 0, bytes, 0);
		final String logMessage = new String(bytes);

		return logMessage;
	}	
	
	void changeSize(GL3 gl, int w, int h) {

		float ratio;
		// Prevent a divide by zero, when window is too short
		// (you cant make a window of zero width).
		if (h == 0)	h = 1;

		// Set the viewport to be the entire window
		gl.glViewport(0, 0, w, h);

		ratio = (1.0f * w) / h;
//		buildProjectionMatrix(70f, ratio, 0.1f, 30.0f);
		buildProjectionMatrix(53.13f, ratio, 1.0f, 30.0f);
	}

	void buildProjectionMatrix(float fov, float ratio, float nearP, float farP) {
		float f = 1.0f / (float) Math.tan(fov * (Math.PI / 360.0));

		projMatrix = Matrix.getIdentityMatrix(4);

		projMatrix.data[0] = f / ratio;
		projMatrix.data[1 * 4 + 1] = f;
		projMatrix.data[2 * 4 + 2] = (farP + nearP) / (nearP - farP);
		projMatrix.data[3 * 4 + 2] = (2.0f * farP * nearP) / (nearP - farP);
		projMatrix.data[2 * 4 + 3] = -1.0f;
		projMatrix.data[3 * 4 + 3] = 0.0f;
	}

}
