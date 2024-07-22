#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include <stdbool.h>
#include"GL/glut.h"

// Global constants
#define ANIMATION_DELAY 40
#define Pi 3.141592654
#define MESH_SIZE 20 
#define SKY_RIGHT 0
#define SKY_LEFT  1
#define SKY_UP    2
#define SKY_DOWN  3
#define SKY_BACK  4
#define SKY_FRONT 5
#define UP true
#define DOWN false

// Rotation variables
int globalRotationAngle = 0, circleViewPointAngle = 30, lengthViewPointAngle = 30;
int fieldOfViewY = 110, centerX = 8, upY = 1, upZ = 0;
float boxRotationAngle = 0.0;
double circleRadius = 0.1, circleYPosition = 1.15;
GLfloat vaneRotationAngle = 0, swingRotationAngle = 0, swingAngle = 0.1;

// Texture IDs
int boxTextureIDs[6];
int nightTextureIDs[6];
int boxTextureID, slideTextureID, redRubberTextureID, ballspoolTextureID, ballTextureID, moonTextureID, earthTextureID, sunTextureID, poolTextureID;
int metalTextureID, metal2TextureID, name1TextureID, name2TextureID, swingTextureID, ropeTextureID, rubberTextureID, woodTextureID, jumpySwingTextureID;
int cabinTextureID, trashTextureID;

// On/off switch
int onOffSwitch = 1, changeTextureSwitch = 1, lightingEnabled = 1;

// Directional flags
bool swingDirection = UP;
bool swing2Direction = UP;
bool ballDirection = UP;
bool isDaytime = true;

// Models system parameters
float solarSystemRotation, sunRadius = 0.7, earthRadius = 0.4, moonRadius = 0.25, sunPosX = 5.7, sunPosY = 6.5, sunPosZ = -4.0;
float ballHeight = 3, ballBoundary, ballRadius, ballScaleFactor = 0, ballScaleFactorX = 1, ballScaleFactorY = 1, ballScaleFactorZ = 1;
float minYScale = 0.5f, maxXZScale = 1.25f;

// Texture filenames
const char* skyTextureFile = "sky.bmp";
const char* grassTextureFile = "grass.bmp";
const char* welcomeTextureFile = "welcome.bmp";
const char* metalTextureFile = "metal.bmp";
const char* metal2TextureFile = "metal2.bmp";
const char* name1TextureFile = "Elroei.bmp";
const char* name2TextureFile = "Roei.bmp";
const char* plasticTextureFile = "plastic.bmp";
const char* ballTextureFile = "balltexture.bmp";
const char* jumpySwingTextureFile = "swing.bmp";
const char* sunTextureFile = "sun.bmp";
const char* earthTextureFile = "earth.bmp";
const char* moonTextureFile = "moon.bmp";
const char* ropeTextureFile = "rope.bmp";
const char* poofTextureFile = "poof.bmp";
const char* ballspoolTextureFile = "ballspool.bmp";
const char* slideTextureFile = "slide.bmp";
const char* nightSkyTextureFile = "nightsky.bmp";
const char* rubberTextureFile = "rubber.bmp";
const char* woodTextureFile = "wood.bmp";
const char* cabinTextureFile = "cabin.bmp";
const char* poolTextureFile = "pool.bmp";
const char* trashTextureFile = "trash.bmp";


// Function declarations
GLuint loadTexture(const char* filename); // Function to load texture
GLubyte* readBMPImage(char* imagepath, int* width, int* height);
void loadTextures();
void handleTerminationError(char* ErrorString);
void drawingCB(void);
void reshapeCB(int width, int height);
void keyboardCB(unsigned char key, int x, int y);
void keyboardSpecialCB(int key, int x, int y);
void TimerCB(int value);
void drawPlain(void);
void drawTexturedSideOfBox(float width, float height, float depth, float texWidth, float texHeight);
void drawTexturedBoxGeometry(float width, float height, float depth, int texture_id);
void drawTexturedBoxWithNames(float width, float height, float depth, int texture_id);
void drawCylinderAndBox(float cylinderRadius, float cylinderHeight, int cylinderSlices, float boxWidth, float boxHeight, float boxDepth);
void drawTexturedCylinderGeometry(float radius, float height, int slices, int TexID);
void drawWindVane(int size);
void drawJumpySwing();
void drawSoccerBall(float top_boundary, float ball_radius);
void drawSolarSystem();
void drawSwing(float height);
void drawPoolBalls(float height, float width, float length);
void drawPool(float height, float width, float length);
void drawSlide(float height);
void drawFerrisWheel(int size, int numCabins);
void drawBench();
void drawTrashCan(float width, float height, float depth);
void drawStreetLamp();
void drawAllLampsBanchTrashs();

int main(int argc, char** argv)
{
    printf("Operating Instructions:\nEsc: exit the program.\n+: Zoom In\n-: Zoom Out \nArrows: changing the point of view in a rotational radius\nX: view change to view from the X axis\nZ: View change to view from the Z axis\n");
    printf("`: lighting 0 - sunlight\n1: Lighting 1 - Moonlight\n2: Lighting 2 - Spot ambient light \n3: lighting lighting 3 - uniform ambient light\n4: Lighting 4 - Spotlight\n5: lighting lighting 5 - hemispherical ambient light\n6: Turn off lighting 0");
    printf("\n7: Turn off lighting 1\n8: Turn off lighting 2\n9: Turn off lighting 3\n0: turning off lighting 4\n=: turning off lighting 5\nL: turning on / off lighting\nS: animation on / off\nD: daylight + daytime sky texture\nN: night lighting + night sky texture\nT: changing the texture of the garden sign\n");
    // Initialize GLUT
    glutInit(&argc, argv);

    // Initializing window
    glutInitWindowSize(500, 500);
    glutInitWindowPosition(900, 100);
    glutInitDisplayMode(GLUT_RGB | GLUT_DEPTH | GLUT_DOUBLE);
    glutCreateWindow("Kid's Park");

    // Enable depth testing
    glEnable(GL_DEPTH_TEST);

    // Enable lighting and light sources
    glEnable(GL_LIGHTING);
    glEnable(GL_LIGHT0);
    glEnable(GL_LIGHT3);

    // Load the texture
    loadTextures();

    // Registering callbacks
    glutDisplayFunc(drawingCB);
    glutReshapeFunc(reshapeCB);
    glutKeyboardFunc(keyboardCB);
    glutSpecialFunc(keyboardSpecialCB); // Registering special key callback
    glutTimerFunc(ANIMATION_DELAY, TimerCB, 0);

    // Start main loop
    glutMainLoop();
}

// Function to load texture
GLuint loadTexture(const char* filename) {
    GLubyte* ImageData;
    int width, height;
    GLuint TextureID;

    //reading the image
    ImageData = readBMPImage(filename, &width, &height);

    //Giving a texture ID
    glGenTextures(1, &TextureID);
    glBindTexture(GL_TEXTURE_2D, TextureID);

    //Assign Image as a texture 
    glTexImage2D(GL_TEXTURE_2D, 0, 3, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, ImageData);

    //freeing image memory
    free(ImageData);

    //setting Specific texture parameters 
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT); // GL_REPEAT or GL_CLAMP
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR); // GL_LINEAR or GL_NEAREST
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

    return TextureID;
}

// Loading textures
void loadTextures() {
    // Day textures
    boxTextureIDs[SKY_RIGHT] = loadTexture(skyTextureFile);
    boxTextureIDs[SKY_LEFT] = loadTexture(skyTextureFile);
    boxTextureIDs[SKY_UP] = loadTexture(skyTextureFile);
    boxTextureIDs[SKY_DOWN] = loadTexture(grassTextureFile);
    boxTextureIDs[SKY_BACK] = loadTexture(skyTextureFile);
    boxTextureIDs[SKY_FRONT] = loadTexture(skyTextureFile);
    boxTextureID = loadTexture(welcomeTextureFile);
    metalTextureID = loadTexture(metalTextureFile);
    metal2TextureID = loadTexture(metal2TextureFile);
    name1TextureID = loadTexture(name1TextureFile);
    name2TextureID = loadTexture(name2TextureFile);
    swingTextureID = loadTexture(plasticTextureFile);
    ballTextureID = loadTexture(ballTextureFile);
    sunTextureID = loadTexture(sunTextureFile);
    earthTextureID = loadTexture(earthTextureFile);
    moonTextureID = loadTexture(moonTextureFile);
    ropeTextureID = loadTexture(ropeTextureFile);
    ballspoolTextureID = loadTexture(ballspoolTextureFile);
    redRubberTextureID = loadTexture(poofTextureFile);
    slideTextureID = loadTexture(slideTextureFile);
    rubberTextureID = loadTexture(rubberTextureFile);
    woodTextureID = loadTexture(woodTextureFile);
    cabinTextureID = loadTexture(cabinTextureFile);
    jumpySwingTextureID = loadTexture(jumpySwingTextureFile);
    poolTextureID = loadTexture(poolTextureFile);
    trashTextureID = loadTexture(trashTextureFile);

    // Night textures
    nightTextureIDs[SKY_RIGHT] = loadTexture(nightSkyTextureFile);
    nightTextureIDs[SKY_LEFT] = loadTexture(nightSkyTextureFile);
    nightTextureIDs[SKY_UP] = loadTexture(nightSkyTextureFile);
    nightTextureIDs[SKY_DOWN] = loadTexture(grassTextureFile);
    nightTextureIDs[SKY_BACK] = loadTexture(nightSkyTextureFile);
    nightTextureIDs[SKY_FRONT] = loadTexture(nightSkyTextureFile);
}

void drawingCB(void)
{
    GLenum er;
    double D = 25;
    double d = 0.02;
    double R = 15;

    // Clearing the background
    glClearColor(0.5, 0.5, 0.5, 0.0);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    // Initializing modelview transformation matrix
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();

    // Set up directional sunlight
    GLfloat sun_direction[] = { 1.0, 1.0, 1.0, 0.0 }; // Direction of sunlight (from point to origin)
    GLfloat sun_color[] = { 1.0, 1.0, 1.0, 1.0 }; // Sunlight color (white)
    GLfloat sun_ambient[] = { 0.3, 0.3, 0.3, 1.0 }; // Sunlight ambient color
    glLightfv(GL_LIGHT0, GL_POSITION, sun_direction);
    glLightfv(GL_LIGHT0, GL_DIFFUSE, sun_color);
    glLightfv(GL_LIGHT0, GL_AMBIENT, sun_ambient); // Adding ambient light
 
    // Set up directional moonlight (LIGHT1)
    GLfloat moon_direction[] = { -1.0, -1.0, -1.0, 0.0 }; // Direction of moonlight (opposite to sunlight)
    GLfloat moon_color[] = { 0.3, 0.3, 0.5, 1.0 }; // Moonlight color (cool tone, e.g., bluish)
    GLfloat moon_ambient[] = { 0.1, 0.1, 0.1, 1.0 }; // Moonlight ambient color
    glLightfv(GL_LIGHT1, GL_POSITION, moon_direction);
    glLightfv(GL_LIGHT1, GL_DIFFUSE, moon_color);
    glLightfv(GL_LIGHT1, GL_AMBIENT, moon_ambient); // Adding ambient light

    // Set up point light source (LIGHT2)
    GLfloat point_position[] = { 0.0, 5.0, 0.0, 1.0 }; // Position of the light source
    GLfloat point_color[] = { 1.0, 1.0, 0.0, 1.0 }; // Light color (yellow)
    glLightfv(GL_LIGHT2, GL_POSITION, point_position);
    glLightfv(GL_LIGHT2, GL_DIFFUSE, point_color);

    // Set up ambient light for uniform lighting
    GLfloat ambient_light[] = { 0.5, 0.5, 0.5, 1.0 }; // Adjust ambient light color as needed
    glLightfv(GL_LIGHT3, GL_AMBIENT, ambient_light);

    // Set up spotlight (LIGHT4)
    GLfloat spot_position[] = { 0.0, 10.0, 0.0, 1.0 }; // Position of the spotlight
    GLfloat spot_direction[] = { 0.0, -1.0, 0.0 }; // Direction of the spotlight
    GLfloat spot_color[] = { 1.0, 0.0, 0.0, 1.0 }; // Spotlight color (red)
    glLightfv(GL_LIGHT4, GL_POSITION, spot_position);
    glLightfv(GL_LIGHT4, GL_SPOT_DIRECTION, spot_direction);
    glLightfv(GL_LIGHT4, GL_DIFFUSE, spot_color);
    glLightf(GL_LIGHT4, GL_SPOT_CUTOFF, 30.0); // Set the cutoff angle of the spotlight

    // Set up hemispherical ambient light (LIGHT5)
    GLfloat hemisphere_ambient[] = { 0.2, 0.2, 0.2, 1.0 }; // Ambient light color for hemispherical lighting
    glLightfv(GL_LIGHT5, GL_AMBIENT, hemisphere_ambient);

    if (lightingEnabled) {
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glEnable(GL_LIGHT3);
    }
    else {
        glDisable(GL_LIGHT0);
        glDisable(GL_LIGHT1);
        glDisable(GL_LIGHT2);
        glDisable(GL_LIGHT3);
        glDisable(GL_LIGHT4);
        glDisable(GL_LIGHT5);
        glDisable(GL_LIGHTING);
    }

    // Setting viewing angle
    gluLookAt(R * cos((double)circleViewPointAngle * Pi / 180), R * sin((double)lengthViewPointAngle * Pi / 180), R * sin((double)circleViewPointAngle * Pi / 180), 0, 0, 0, 0, upY, upZ);

    // SkyBox
    {
        //right
        glBindTexture(GL_TEXTURE_2D, isDaytime ? boxTextureIDs[SKY_RIGHT] : nightTextureIDs[SKY_RIGHT]);
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0); glVertex3f(+D - d, -D, -D);
        glTexCoord2f(1, 0); glVertex3f(+D - d, -D, +D);
        glTexCoord2f(1, 1); glVertex3f(+D - d, +D, +D);
        glTexCoord2f(0, 1); glVertex3f(+D - d, +D, -D);
        glEnd();
        //left
        glBindTexture(GL_TEXTURE_2D, isDaytime ? boxTextureIDs[SKY_LEFT] : nightTextureIDs[SKY_LEFT]);
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0); glVertex3f(-D + d, -D, +D);
        glTexCoord2f(1, 0); glVertex3f(-D + d, -D, -D);
        glTexCoord2f(1, 1); glVertex3f(-D + d, +D, -D);
        glTexCoord2f(0, 1); glVertex3f(-D + d, +D, +D);
        glEnd();
        //up
        glBindTexture(GL_TEXTURE_2D, isDaytime ? boxTextureIDs[SKY_UP] : nightTextureIDs[SKY_UP]);
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0); glVertex3f(-D, +D - d, -D);
        glTexCoord2f(1, 0); glVertex3f(+D, +D - d, -D);
        glTexCoord2f(1, 1); glVertex3f(+D, +D - d, +D);
        glTexCoord2f(0, 1); glVertex3f(-D, +D - d, +D);
        glEnd();
        //down
        glBindTexture(GL_TEXTURE_2D, isDaytime ? boxTextureIDs[SKY_DOWN] : nightTextureIDs[SKY_DOWN]);
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0); glVertex3f(-D, -D + d, +D);
        glTexCoord2f(1, 0); glVertex3f(+D, -D + d, +D);
        glTexCoord2f(1, 1); glVertex3f(+D, -D + d, -D);
        glTexCoord2f(0, 1); glVertex3f(-D, -D + d, -D);
        glEnd();
        //back
        glBindTexture(GL_TEXTURE_2D, isDaytime ? boxTextureIDs[SKY_BACK] : nightTextureIDs[SKY_BACK]);
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0); glVertex3f(-D, -D, -D + d);
        glTexCoord2f(1, 0); glVertex3f(+D, -D, -D + d);
        glTexCoord2f(1, 1); glVertex3f(+D, +D, -D + d);
        glTexCoord2f(0, 1); glVertex3f(-D, +D, -D + d);
        glEnd();
        //front
        glBindTexture(GL_TEXTURE_2D, isDaytime ? boxTextureIDs[SKY_FRONT] : nightTextureIDs[SKY_FRONT]);
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0); glVertex3f(+D, -D, +D - d);
        glTexCoord2f(1, 0); glVertex3f(-D, -D, +D - d);
        glTexCoord2f(1, 1); glVertex3f(-D, +D, +D - d);
        glTexCoord2f(0, 1); glVertex3f(+D, +D, +D - d);
        glEnd();
    }

    // Drawing objects
    drawPlain();
    glPushMatrix();
    glTranslatef(2, 0, 2);
    glScalef(1.5, 1.5, 1.5);
    drawCylinderAndBox(0.2, 4, 80, 3, 1, 3);
    glPopMatrix();
    glPushMatrix();
    glTranslatef(-15, 0, 18);
    glRotatef(90, 0, 1, 0);
    drawWindVane(2);
    glPopMatrix();
    drawAllLampsBanchTrashs();
    glPushMatrix();
    glTranslatef(10, 0, -10);
    glScalef(2.5, 2.5, 2.5);
    drawFerrisWheel(2, 8);
    glPopMatrix();
    glPushMatrix();
    glTranslatef(-3, 0, 15);
    drawJumpySwing();
    glPopMatrix();
    glPushMatrix();
    glScalef(0.5, 0.5, 0.5);
    glTranslatef(-4, 0, 8);
    drawSoccerBall(5, 1);
    glPopMatrix();
    glPushMatrix();
    glRotatef(solarSystemRotation, 0, 1, 0);
    glTranslatef(-10,10,-10);
    drawSolarSystem();
    glPopMatrix();
    glPushMatrix();
    glTranslatef(-5, 0, -10);
    drawSwing(2);
    glTranslatef(-5, 0, 0);
    drawSwing(2);
    glPopMatrix();
    glPushMatrix();
    glTranslatef(8, 0, 12.4);
    drawPool(0.5, 6, 6);
    glPopMatrix();
    glPushMatrix();
    glTranslatef(-14, 0, 2);
    drawSlide(3);
    glPopMatrix();
    glEnable(GL_TEXTURE_2D);
    // Swapping buffers and displaying
    glutSwapBuffers();

    // Check for errors
    er = glGetError();  // Get errors. 0 for no error
    if (er) printf("error: %d\n", er);
}

void reshapeCB(int width, int height)
{
    float zNear, zFar;

    // Define ortho
    zNear = 0.1; zFar = 50;

    // Update viewport
    glViewport(0, 0, width, height);

    // Clear the transformation matrices (load identity)
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();

    // Projection
    gluPerspective(fieldOfViewY, 1, zNear, zFar);
}

void keyboardCB(unsigned char key, int x, int y) {
    switch (key) {
    case 27: // ESC key to exit
        exit(1);
        break;
    case '+': // Zoom in
        fieldOfViewY = fieldOfViewY - 1;
        reshapeCB(glutGet(GLUT_WINDOW_WIDTH), glutGet(GLUT_WINDOW_HEIGHT));
        break;
    case '-': // Zoom out
        fieldOfViewY = fieldOfViewY + 1;
        reshapeCB(glutGet(GLUT_WINDOW_WIDTH), glutGet(GLUT_WINDOW_HEIGHT));
        break;
    case 'x':
    case 'X': // Change perspective to view from positive X-axis
        circleRadius = 8;
        circleYPosition = 0;
        centerX = 0;
        upY = 1;
        upZ = 0;
        break;
    case 'z':
    case 'Z': // Change perspective to view from positive Z-axis
        circleRadius = 8;
        circleYPosition = 8;
        centerX = 0;
        upY = 0;
        upZ = 1;
        break;
    case '`':
        glEnable(GL_LIGHT0);
        break;
    case '1':
        glEnable(GL_LIGHT1);
        break;
    case '2': 
        glEnable(GL_LIGHT2);
        break;
    case '3':
        glEnable(GL_LIGHT3);
        break;
    case '4':
        glEnable(GL_LIGHT4);
        break;
    case '5':
        glEnable(GL_LIGHT5);
        break;
    case '6':
        glDisable(GL_LIGHT0);
        break;
    case '7':
        glDisable(GL_LIGHT1);
        break;
    case '8':
        glDisable(GL_LIGHT2);
        break;
    case '9':
        glDisable(GL_LIGHT3);
        break;
    case '0':
        glDisable(GL_LIGHT4);
        break;
    case '=':
        glDisable(GL_LIGHT5);
        break;
    case 'l':
    case 'L':// Toggle lighting on
        lightingEnabled = !lightingEnabled;
        glutPostRedisplay(); // Request a redraw to reflect the change
        break;
    case 's':
    case 'S':
        onOffSwitch = !onOffSwitch;
        break;
    case 'd':
    case 'D':
        isDaytime = true;
        glEnable(GL_LIGHT0);
        glEnable(GL_LIGHT3);
        glDisable(GL_LIGHT1);
        glDisable(GL_LIGHT2);
        glDisable(GL_LIGHT4);
        glDisable(GL_LIGHT5);
        break;
    case 'n':
    case 'N':
        isDaytime = false;
        glEnable(GL_LIGHT1);
        glEnable(GL_LIGHT3);
        glDisable(GL_LIGHT0);
        glDisable(GL_LIGHT2);
        glDisable(GL_LIGHT4);
        glDisable(GL_LIGHT5);
        break;
    case 't':
    case 'T':
    if(changeTextureSwitch) changeTextureSwitch = !changeTextureSwitch;
    else
        changeTextureSwitch =1;
        break;
    }
}

void keyboardSpecialCB(int key, int x, int y)
{
    switch (key) {
    case GLUT_KEY_LEFT:
        circleViewPointAngle = (circleViewPointAngle + 360 + 1) % 360;
        glutPostRedisplay();
        break;
    case GLUT_KEY_RIGHT:
        circleViewPointAngle = (circleViewPointAngle + 360 - 1) % 360;
        glutPostRedisplay();
        break;
    case GLUT_KEY_UP:
        // Increase lengthViewPointAngle, but limit it to be between 10 and 90 degrees
        if (lengthViewPointAngle < 90)
            lengthViewPointAngle = fmin(90, lengthViewPointAngle + 1);
        glutPostRedisplay();
        break;
    case GLUT_KEY_DOWN:
        // Decrease lengthViewPointAngle, but limit it to be between 10 and 90 degrees
        if (lengthViewPointAngle > 10)
            lengthViewPointAngle = fmax(10, lengthViewPointAngle - 1);
        glutPostRedisplay();
        break;
    }

}

void TimerCB(int value) {
    // Increment global rotation angle for general animations
    globalRotationAngle += 3;

    // Increment box rotation angle for specific animations
    boxRotationAngle += 0.5f;

    // Increment vane rotation angle for specific animations
    vaneRotationAngle += 1.0f;

    // Increment or decrement swing_angle based on its current value and direction
    if (swingDirection) {
        swingAngle += 0.3;
        if (swingAngle >= 12.9) {
            swingDirection = !swingDirection; // Change direction if swing angle reaches maximum
        }
    }
    else {
        swingAngle -= 0.3;
        if (swingAngle <= -12.9) {
            swingDirection = !swingDirection; // Change direction if swing angle reaches minimum
        }
    }

    // Ball and Swing Matching
    globalRotationAngle = (globalRotationAngle + 360 + 1) % 360; // Ensure global rotation angle stays within 0 to 360 degrees range

    if (ballDirection) {
        ballHeight += 0.2;
        if (ballHeight >= ballBoundary) {
            ballDirection = !ballDirection; // Change direction if ball reaches upper boundary
        }

    }
    else {
        ballHeight -= 0.2;
        if (ballHeight <= 0.25 * ballRadius) {
            ballDirection = !ballDirection; // Change direction if ball reaches lower boundary
        }
    }

    // Apply scaling factors based on ball's position and direction
    if (ballHeight <= ballRadius && !ballDirection && ballHeight >= 0.5 * ballRadius) {
        ballScaleFactorX = fminf(ballScaleFactorX + 0.1f, maxXZScale * ballRadius);
        ballScaleFactorZ = fminf(ballScaleFactorZ + 0.1f, maxXZScale * ballRadius);
        ballScaleFactorY = fmaxf(ballScaleFactorY - 0.1f, minYScale * ballRadius);
    }
    else if (ballHeight >= 0.5 * ballRadius && ballDirection && ballHeight <= ballRadius) {
        ballScaleFactorX = fmaxf(ballScaleFactorX - 0.1f, 1.0f);
        ballScaleFactorZ = fmaxf(ballScaleFactorZ - 0.1f, 1.0f);
        ballScaleFactorY = fminf(ballScaleFactorY + 0.1f, 1.0f);
    }

    if (ballHeight >= (ballBoundary - 0.5 * ballRadius) && ballDirection && ballHeight <= ballBoundary) {
        // Apply the same logic for scaling when the ball is at the top boundary
        ballScaleFactorX = fminf(ballScaleFactorX + 0.1f, maxXZScale * ballRadius);
        ballScaleFactorZ = fminf(ballScaleFactorZ + 0.1f, maxXZScale * ballRadius);
        ballScaleFactorY = fmaxf(ballScaleFactorY - 0.1f, minYScale * ballRadius);
    }
    else if (ballHeight <= ballBoundary && !ballDirection && ballHeight >= ballBoundary - 0.5 * ballRadius) {
        ballScaleFactorX = fmaxf(ballScaleFactorX - 0.1f, 1.0f);
        ballScaleFactorZ = fmaxf(ballScaleFactorZ - 0.1f, 1.0f);
        ballScaleFactorY = fminf(ballScaleFactorY + 0.1f, 1.0f);
    }

    // Reassign the timer event for continuous animation
    glutTimerFunc(ANIMATION_DELAY, TimerCB, value);

    // Trigger redisplay to update the scene
    glutPostRedisplay();
}

void drawPlain(void)
{
    // Enable texturing
    glEnable(GL_TEXTURE_2D);
    glBindTexture(GL_TEXTURE_2D, boxTextureIDs[SKY_DOWN]); // Bind the texture

    // Draw the plane
    glBegin(GL_QUADS);
    glTexCoord2f(0.0, 0.0); glVertex3f(-MESH_SIZE, 0, -MESH_SIZE);
    glTexCoord2f(1.0, 0.0); glVertex3f(MESH_SIZE, 0, -MESH_SIZE);
    glTexCoord2f(1.0, 1.0); glVertex3f(MESH_SIZE, 0, MESH_SIZE);
    glTexCoord2f(0.0, 1.0); glVertex3f(-MESH_SIZE, 0, MESH_SIZE);
    glEnd();

    glDisable(GL_TEXTURE_2D);
}

void drawTexturedCylinderGeometry(float radius, float height, int slices, int TexID) {
    int i;
    glEnable(GL_LIGHTING);
    // Set material properties for a metallic appearance
    GLfloat mat_ambient[] = { 0.25, 0.25, 0.25, 1.0 };
    GLfloat mat_diffuse[] = { 0.4, 0.4, 0.4, 1.0 };
    GLfloat mat_specular[] = { 0.774597, 0.774597, 0.774597, 1.0 };
    GLfloat mat_shininess[] = { 76.0 }; // Shininess of the material

    // Set material properties
    glMaterialfv(GL_FRONT, GL_AMBIENT, mat_ambient);
    glMaterialfv(GL_FRONT, GL_DIFFUSE, mat_diffuse);
    glMaterialfv(GL_FRONT, GL_SPECULAR, mat_specular);
    glMaterialfv(GL_FRONT, GL_SHININESS, mat_shininess);

    // Enable texture mapping
    glEnable(GL_TEXTURE_2D);
    glBindTexture(GL_TEXTURE_2D, TexID);

    // Draw the cylinder with texture coordinates
    glBegin(GL_QUAD_STRIP);
    for (i = 0; i <= slices; i++) {
        float angle = 2.0f * Pi * i / slices;
        float x = radius * cos(angle);
        float z = radius * sin(angle);

        glNormal3f(x / radius, 0.0, z / radius);

        // Texture coordinates
        float s = (float)i / slices;
        glTexCoord2f(s, 0.0f);
        glVertex3f(x, 0.0, z);
        glTexCoord2f(s, 1.0f);
        glVertex3f(x, height, z);
    }
    glEnd();

    // Disable texture mapping
    glDisable(GL_TEXTURE_2D);
   glDisable(GL_LIGHTING);

}

void drawCylinderAndBox(float cylinderRadius, float cylinderHeight, int cylinderSlices, float boxWidth, float boxHeight, float boxDepth) {
    float totalHeight = cylinderHeight + boxHeight; // Calculate the combined height of the cylinder and the box

    // Draw the cylinder first
    drawTexturedCylinderGeometry(cylinderRadius, cylinderHeight, cylinderSlices, metal2TextureID);

    // Save the current transformation matrix
    glPushMatrix();

    // Apply translation and rotation to position and orient the box
    glTranslatef(0.0, totalHeight - 1, 0.0); // Move to the top center position
    if (onOffSwitch) {
        glRotatef(boxRotationAngle, 0.0f, 1.0f, 0.0f); // Rotate the box around the y-axis if the switch is on
    }

    // Check if texture change switch is on
    if (changeTextureSwitch) {
        // Draw the textured box with names instead of the regular textured box
        drawTexturedBoxWithNames(boxWidth, boxHeight, boxDepth, boxTextureID);
    }
    else {
        // Draw the regular textured box
        drawTexturedBoxGeometry(boxWidth, boxHeight, boxDepth, boxTextureID);
    }

    // Restore the transformation matrix
    glPopMatrix();
}


// Function to draw a single side of the box with texture mapping
void drawTexturedBoxGeometry(float width, float height, float depth, int texture_id) {
    // Enable texture 2D
    glEnable(GL_TEXTURE_2D);

    // Bind the texture for the box sides
    glBindTexture(GL_TEXTURE_2D, texture_id);

    // Enable lighting
    glEnable(GL_LIGHTING);

    // Set material properties
    GLfloat mat_ambient[] = { 0.7, 0.7, 0.7, 1.0 };
    GLfloat mat_diffuse[] = { 0.8, 0.8, 0.8, 1.0 };
    GLfloat mat_specular[] = { 1.0, 1.0, 1.0, 1.0 };
    GLfloat mat_shininess[] = { 100.0 }; // Shininess of the material

    glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, mat_ambient);
    glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, mat_diffuse);
    glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, mat_specular);
    glMaterialfv(GL_FRONT_AND_BACK, GL_SHININESS, mat_shininess);

    // Draw the box sides
    glBegin(GL_QUADS);

    // Front face
    glTexCoord2f(0, 0); glVertex3f(-width / 2, 0.0, depth / 2);
    glTexCoord2f(1, 0); glVertex3f(width / 2, 0.0, depth / 2);
    glTexCoord2f(1, 1); glVertex3f(width / 2, height, depth / 2);
    glTexCoord2f(0, 1); glVertex3f(-width / 2, height, depth / 2);

    // Back face
    glTexCoord2f(0, 0); glVertex3f(width / 2, 0.0, -depth / 2);
    glTexCoord2f(1, 0); glVertex3f(-width / 2, 0.0, -depth / 2);
    glTexCoord2f(1, 1); glVertex3f(-width / 2, height, -depth / 2);
    glTexCoord2f(0, 1); glVertex3f(width / 2, height, -depth / 2);

    // Top face
    glTexCoord2f(0, 0); glVertex3f(-width / 2, height, -depth / 2);
    glTexCoord2f(1, 0); glVertex3f(width / 2, height, -depth / 2);
    glTexCoord2f(1, 1); glVertex3f(width / 2, height, depth / 2);
    glTexCoord2f(0, 1); glVertex3f(-width / 2, height, depth / 2);

    // Bottom face
    glTexCoord2f(0, 0); glVertex3f(-width / 2, 0.0, -depth / 2);
    glTexCoord2f(1, 0); glVertex3f(-width / 2, 0.0, depth / 2);
    glTexCoord2f(1, 1); glVertex3f(width / 2, 0.0, depth / 2);
    glTexCoord2f(0, 1); glVertex3f(width / 2, 0.0, -depth / 2);

    // Draw the long box
    // Left face
    glTexCoord2f(0, 0); glVertex3f(-width / 2, 0.0, -depth / 2);
    glTexCoord2f(1, 0); glVertex3f(-width / 2, 0.0, depth / 2);
    glTexCoord2f(1, 1); glVertex3f(-width / 2, height, depth / 2);
    glTexCoord2f(0, 1); glVertex3f(-width / 2, height, -depth / 2);


    // Right face
    glTexCoord2f(0, 0); glVertex3f(width / 2, 0.0, depth / 2);
    glTexCoord2f(1, 0); glVertex3f(width / 2, 0.0, -depth / 2);
    glTexCoord2f(1, 1); glVertex3f(width / 2, height, -depth / 2);
    glTexCoord2f(0, 1); glVertex3f(width / 2, height, depth / 2);
    glEnd();


    // Disable lighting
   glDisable(GL_LIGHTING);

    // Disable texture 2D
    glDisable(GL_TEXTURE_2D);
}

void drawTexturedBoxWithNames(float width, float height, float depth, int texture_id) {
    // Enable texture 2D
    glEnable(GL_TEXTURE_2D);

    // Bind the texture for the box sides
    glBindTexture(GL_TEXTURE_2D, texture_id);

    // Enable lighting
   glEnable(GL_LIGHTING);

    // Set material properties
    GLfloat mat_ambient[] = { 0.7, 0.7, 0.7, 1.0 };
    GLfloat mat_diffuse[] = { 0.8, 0.8, 0.8, 1.0 };
    GLfloat mat_specular[] = { 1.0, 1.0, 1.0, 1.0 };
    GLfloat mat_shininess[] = { 100.0 }; // Shininess of the material

    glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, mat_ambient);
    glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, mat_diffuse);
    glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, mat_specular);
    glMaterialfv(GL_FRONT_AND_BACK, GL_SHININESS, mat_shininess);

    // Draw the box sides
    glBegin(GL_QUADS);

    // Front face
    glTexCoord2f(0, 0); glVertex3f(-width / 2, 0.0, depth / 2);
    glTexCoord2f(1, 0); glVertex3f(width / 2, 0.0, depth / 2);
    glTexCoord2f(1, 1); glVertex3f(width / 2, height, depth / 2);
    glTexCoord2f(0, 1); glVertex3f(-width / 2, height, depth / 2);

    // Back face
    glTexCoord2f(0, 0); glVertex3f(width / 2, 0.0, -depth / 2);
    glTexCoord2f(1, 0); glVertex3f(-width / 2, 0.0, -depth / 2);
    glTexCoord2f(1, 1); glVertex3f(-width / 2, height, -depth / 2);
    glTexCoord2f(0, 1); glVertex3f(width / 2, height, -depth / 2);

    // Top face
    glTexCoord2f(0, 0); glVertex3f(-width / 2, height, -depth / 2);
    glTexCoord2f(1, 0); glVertex3f(width / 2, height, -depth / 2);
    glTexCoord2f(1, 1); glVertex3f(width / 2, height, depth / 2);
    glTexCoord2f(0, 1); glVertex3f(-width / 2, height, depth / 2);

    // Bottom face
    glTexCoord2f(0, 0); glVertex3f(-width / 2, 0.0, -depth / 2);
    glTexCoord2f(1, 0); glVertex3f(-width / 2, 0.0, depth / 2);
    glTexCoord2f(1, 1); glVertex3f(width / 2, 0.0, depth / 2);
    glTexCoord2f(0, 1); glVertex3f(width / 2, 0.0, -depth / 2);

     glEnd();
     glBindTexture(GL_TEXTURE_2D, name1TextureID);

     // Draw the long box
    glBegin(GL_QUADS);
     // Left face
    glTexCoord2f(0, 0); glVertex3f(-width / 2, 0.0, -depth / 2);
    glTexCoord2f(1, 0); glVertex3f(-width / 2, 0.0, depth / 2);
    glTexCoord2f(1, 1); glVertex3f(-width / 2, height, depth / 2);
    glTexCoord2f(0, 1); glVertex3f(-width / 2, height, -depth / 2);

      glEnd();
     glBindTexture(GL_TEXTURE_2D, name2TextureID);
      glBegin(GL_QUADS);
      // Right face
    glTexCoord2f(0, 0); glVertex3f(width / 2, 0.0, depth / 2);
    glTexCoord2f(1, 0); glVertex3f(width / 2, 0.0, -depth / 2);
    glTexCoord2f(1, 1); glVertex3f(width / 2, height, -depth / 2);
    glTexCoord2f(0, 1); glVertex3f(width / 2, height, depth / 2);
    glEnd();


    // Disable lighting
    glDisable(GL_LIGHTING);

    // Disable texture 2D
    glDisable(GL_TEXTURE_2D);
}

void drawTexturedSideOfBox(float width, float height, float depth, float texWidth, float texHeight) {
    glBegin(GL_QUADS); // Begin drawing a quadrilateral (a side of the box)

    // Set texture coordinates and vertices for the quadrilateral
    glTexCoord2f(0.0, 0.0); glVertex3f(-width / 2, -height / 2, depth / 2); // Bottom left corner
    glTexCoord2f(texWidth, 0.0); glVertex3f(width / 2, -height / 2, depth / 2); // Bottom right corner
    glTexCoord2f(texWidth, texHeight); glVertex3f(width / 2, height / 2, depth / 2); // Top right corner
    glTexCoord2f(0.0, texHeight); glVertex3f(-width / 2, height / 2, depth / 2); // Top left corner

    glEnd(); // End drawing the quadrilateral
}

void drawWindVane(int size) {

    glPushMatrix(); // Save the current matrix state

    glPushMatrix(); // Save the current matrix state
    glTranslatef(0, 0, -0.15); // Translate to the base position
    // Draw the cylindrical base of the wind vane
    drawTexturedCylinderGeometry(0.3f, size * 2, 70, metal2TextureID);
    glPopMatrix(); // Restore the matrix state

    // Enable lighting
    glEnable(GL_LIGHTING);

    glTranslatef(0, size * 2, 0); // Translate to the sphere position
    // Draw the spherical top of the wind vane
    glutSolidSphere(0.25, 10, 15);

    if (onOffSwitch) {
        glRotatef(vaneRotationAngle, 0, 0, 1); // Rotate the wind vane if the switch is on
    }
    glScalef(size, size, size); // Scale the wind vane

    GLfloat colors[6][4] = {
        {1.0f, 0.0f, 0.0f, 1.0f}, // Red
        {0.0f, 1.0f, 0.0f, 1.0f}, // Green
        {0.0f, 0.0f, 1.0f, 1.0f}, // Blue
        {1.0f, 1.0f, 0.0f, 1.0f}, // Yellow
        {1.0f, 0.0f, 1.0f, 1.0f}, // Magenta
        {0.0f, 1.0f, 1.0f, 1.0f}  // Cyan
    };

    for (int i = 0; i < 6; i++) {
        glPushMatrix(); // Save the current matrix state

        // Set the material properties to use different colors
        GLfloat material_ambient[] = { colors[i][0], colors[i][1], colors[i][2], 1.0f };
        GLfloat material_diffuse[] = { colors[i][0], colors[i][1], colors[i][2], 1.0f };
        GLfloat material_specular[] = { 0.8f, 0.8f, 0.8f, 1.0f }; // Specular properties remain constant for some shine
        GLfloat material_shininess[] = { 30.0f }; // Uniform shininess

        glMaterialfv(GL_FRONT, GL_AMBIENT, material_ambient);
        glMaterialfv(GL_FRONT, GL_DIFFUSE, material_diffuse);
        glMaterialfv(GL_FRONT, GL_SPECULAR, material_specular);
        glMaterialfv(GL_FRONT, GL_SHININESS, material_shininess);

        // Draw the front face of the triangle
        glBegin(GL_TRIANGLES);
        glTexCoord2f(0.0f, 0.0f); glVertex3f(0, 0, 0);     // Vertex A
        glTexCoord2f(0.0f, 1.0f); glVertex3f(0, 1, 0);     // Vertex B
        glTexCoord2f(1.0f, 1.0f); glVertex3f(1, 1, 0);     // Vertex C
        glEnd();

        // Draw the back face of the triangle
        glBegin(GL_TRIANGLES);
        glTexCoord2f(0.0f, 0.0f); glVertex3f(0, 0, 0.1);   // Vertex D
        glTexCoord2f(0.0f, 1.0f); glVertex3f(0, 1, 0.1);   // Vertex E
        glTexCoord2f(1.0f, 1.0f); glVertex3f(1, 1, 0.1);   // Vertex F
        glEnd();

        // Draw the side faces of the triangle
        glBegin(GL_QUADS);
        // Side face 1: A -> B -> E -> D
        glVertex3f(0, 0, 0);     // Vertex A
        glVertex3f(0, 1, 0);     // Vertex B
        glVertex3f(0, 1, 0.1);   // Vertex E
        glVertex3f(0, 0, 0.1);   // Vertex D
        // Side face 2: A -> D -> F -> C
        glVertex3f(0, 0, 0);     // Vertex A
        glVertex3f(0, 0, 0.1);   // Vertex D
        glVertex3f(1, 1, 0.1);   // Vertex F
        glVertex3f(1, 1, 0);     // Vertex C
        // Side face 3: B -> E -> F -> C
        glVertex3f(0, 1, 0);     // Vertex B
        glVertex3f(0, 1, 0.1);   // Vertex E
        glVertex3f(1, 1, 0.1);   // Vertex F
        glVertex3f(1, 1, 0);     // Vertex C
        glEnd();

        glPopMatrix(); // Restore the matrix state
        glRotatef(60, 0, 0, 1); // Rotate for the next triangle
    }

    glPopMatrix(); // Restore the matrix state
    glDisable(GL_LIGHTING);
}

void drawJumpySwing() {
    glDisable(GL_TEXTURE_2D); // Disable 2D texturing
    glEnable(GL_LIGHTING); // Enable lighting for the scene
    glPushMatrix(); // Save the current matrix state

    glPushMatrix(); // Save the current matrix state
    // Swing base
    GLfloat rubberAmbient[] = { 0.05f, 0.05f, 0.05f, 1.0f };
    GLfloat rubberDiffuse[] = { 0.5f, 0.5f, 0.5f, 1.0f };
    GLfloat rubberSpecular[] = { 0.7f, 0.7f, 0.7f, 1.0f };
    GLfloat rubberShininess = 10.0f;

    // Set material properties for rubber
    glMaterialfv(GL_FRONT, GL_AMBIENT, rubberAmbient);
    glMaterialfv(GL_FRONT, GL_DIFFUSE, rubberDiffuse);
    glMaterialfv(GL_FRONT, GL_SPECULAR, rubberSpecular);
    glMaterialf(GL_FRONT, GL_SHININESS, rubberShininess);

    // Draw swing base
    glBegin(GL_TRIANGLES);
    glVertex3f(0, 1, 0);
    glVertex3f(-0.5, 0, 0);
    glVertex3f(0.5, 0, 0);

    glVertex3f(0, 1, -1);
    glVertex3f(-0.5, 0, -1);
    glVertex3f(0.5, 0, -1);
    glEnd();

    glBegin(GL_QUADS);
    glVertex3f(0, 1, 0);
    glVertex3f(0, 1, -1);
    glVertex3f(-0.5, 0, -1);
    glVertex3f(-0.5, 0, 0);

    glVertex3f(0, 1, 0);
    glVertex3f(0, 1, -1);
    glVertex3f(0.5, 0, -1);
    glVertex3f(0.5, 0, 0);

    glVertex3f(-0.5, 0, -1);
    glVertex3f(-0.5, 0, 0);
    glVertex3f(0.5, 0, -1);
    glVertex3f(0.5, 0, 0);
    glEnd();

    glPopMatrix(); // Restore the matrix state

    // Swing seats
    if (onOffSwitch) {
        glRotatef(swingAngle, 0, 0, 1); // Rotate the seats
    }
    glPushMatrix(); // Save the current matrix state
    glTranslatef(0, 1, -0.5); // Translate to seats' position
    drawTexturedBoxGeometry(7, 0.2, 1, jumpySwingTextureID); // Draw seats
    glPopMatrix(); // Restore the matrix state

    // Handles
    glPushMatrix(); // Save the current matrix state
    glDisable(GL_TEXTURE_2D); // Disable 2D texturing
    glTranslatef(-2.4, 1.2, -0.5); // Translate to first handle position
    drawTexturedCylinderGeometry(0.1, 0.2, 50, metal2TextureID); // Draw first handle
    glTranslatef(4.8, 0, 0); // Translate to second handle position
    drawTexturedCylinderGeometry(0.1, 0.2, 50, metal2TextureID); // Draw second handle
    glEnable(GL_TEXTURE_2D); // Enable 2D texturing
    glPopMatrix(); // Restore the matrix state

    glPushMatrix(); // Save the current matrix state
    glDisable(GL_TEXTURE_2D); // Disable 2D texturing
    glTranslatef(2.4, 1.5, -1); // Translate to third handle position
    glRotatef(90, 1, 0, 0); // Rotate the handle
    drawTexturedCylinderGeometry(0.12, 1, 50, metal2TextureID); // Draw third handle
    glTranslatef(-4.8, 0, 0); // Translate to fourth handle position
    drawTexturedCylinderGeometry(0.12, 1, 50, metal2TextureID); // Draw fourth handle
    glEnable(GL_TEXTURE_2D); // Enable 2D texturing
    glPopMatrix(); // Restore the matrix state

    glPopMatrix(); // Restore the matrix state
    glEnable(GL_TEXTURE_2D); // Enable 2D texturing
    glDisable(GL_LIGHTING); // Disable lighting for the scene
}


void drawSoccerBall(float top_boundary, float ball_radius) {
    // Set boundary and radius for the ball
    ballBoundary = top_boundary;
    ballRadius = ball_radius;

    glPushMatrix(); // Save the current matrix state
   glEnable(GL_LIGHTING); // Enable lighting for the scene
    glPushMatrix(); // Save the current matrix state
    glBindTexture(GL_TEXTURE_2D, ballTextureID); // Bind the texture for the ball
    glTexGenf(GL_S, GL_TEXTURE_GEN_MODE, GL_SPHERE_MAP); // Set texture generation mode for S coordinate
    glTexGenf(GL_T, GL_TEXTURE_GEN_MODE, GL_SPHERE_MAP); // Set texture generation mode for T coordinate
    glEnable(GL_TEXTURE_GEN_S); // Enable texture coordinate generation for S
    glEnable(GL_TEXTURE_GEN_T); // Enable texture coordinate generation for T

    if (onOffSwitch) {
        // Translate the ball to its current position
        glTranslatef(0, ballHeight, 0);
        // Scale the ball
        glScalef(ballScaleFactorX, ballScaleFactorY, ballScaleFactorZ);
    }

    // Draw the ball as a solid sphere
    glutSolidSphere(ball_radius, 100, 100);

    glDisable(GL_TEXTURE_GEN_S); // Disable texture coordinate generation for S
    glDisable(GL_TEXTURE_GEN_T); // Disable texture coordinate generation for T
    glPopMatrix(); // Restore the matrix state

    glPopMatrix(); // Restore the matrix state
  glDisable(GL_LIGHTING); // Disable lighting for the scene
}


void drawSolarSystem() {
    // Enable lighting for the scene
  glEnable(GL_LIGHTING);

    // Set light properties for the sun
    GLfloat light_position1[] = { sunPosX, sunPosY, sunPosZ, 1.0 };
    GLfloat light_diffuse_sphere1[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    GLfloat light_specular_sphere1[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    glLightfv(GL_LIGHT3, GL_POSITION, light_position1);
    glLightfv(GL_LIGHT3, GL_DIFFUSE, light_diffuse_sphere1);
    glLightfv(GL_LIGHT3, GL_SPECULAR, light_specular_sphere1);

    // Draw the sun
    glPushMatrix();
    glBindTexture(GL_TEXTURE_2D, sunTextureID);
    glTexGenf(GL_S, GL_TEXTURE_GEN_MODE, GL_SPHERE_MAP);
    glTexGenf(GL_T, GL_TEXTURE_GEN_MODE, GL_SPHERE_MAP);
    glEnable(GL_TEXTURE_GEN_S);
    glEnable(GL_TEXTURE_GEN_T);
    if(onOffSwitch)
    glRotated(globalRotationAngle, 0.0, 1, 0.0); // Rotate the sun
    glutSolidSphere(sunRadius, 100, 100); // Draw the sun as a solid sphere
    glDisable(GL_TEXTURE_GEN_S);
    glDisable(GL_TEXTURE_GEN_T);

    // Draw the Earth
    glBindTexture(GL_TEXTURE_2D, earthTextureID);
    glTexGenf(GL_S, GL_TEXTURE_GEN_MODE, GL_SPHERE_MAP);
    glTexGenf(GL_T, GL_TEXTURE_GEN_MODE, GL_SPHERE_MAP);
    glEnable(GL_TEXTURE_GEN_S);
    glEnable(GL_TEXTURE_GEN_T);
    if (onOffSwitch)
    glRotated(globalRotationAngle, 0.0, 1, 0.0); // Rotate the Earth
    glTranslatef(2.5, 0, 0); // Translate to Earth's position
    glutSolidSphere(earthRadius, 100, 100); // Draw Earth as a solid sphere
    glDisable(GL_TEXTURE_GEN_S);
    glDisable(GL_TEXTURE_GEN_T);

    // Draw the Moon rotating around the Earth
    glBindTexture(GL_TEXTURE_2D, moonTextureID);
    glTexGenf(GL_S, GL_TEXTURE_GEN_MODE, GL_SPHERE_MAP);
    glTexGenf(GL_T, GL_TEXTURE_GEN_MODE, GL_SPHERE_MAP);
    glEnable(GL_TEXTURE_GEN_S);
    glEnable(GL_TEXTURE_GEN_T);
    if (onOffSwitch)
    glRotated(globalRotationAngle * 2.0, 0, 1, 0.0); // Rotate the Moon around the Earth
    glPushMatrix();
    glTranslatef(0.8, 0, 0); // Translate to Moon's position relative to Earth
    glutSolidSphere(moonRadius, 100, 100); // Draw the Moon as a solid sphere
    glDisable(GL_TEXTURE_GEN_S);
    glDisable(GL_TEXTURE_GEN_T);
    glPopMatrix();

    glPopMatrix(); // Restore the original matrix
    glDisable(GL_LIGHTING); // Disable lighting for the scene
}

void drawSwing(float height) {
  glEnable(GL_LIGHTING);
    glPushMatrix();
    glDisable(GL_TEXTURE_2D);

    //Body
    glPushMatrix();
    glPushMatrix();
    glTranslatef(0, 0, -0.7);
    glTranslatef(3, 0, 0);
    glRotatef(15, 1, 0, 0);
    drawTexturedBoxGeometry(0.2, 5.2, 0.2, metalTextureID);
    glTranslatef(-6, 0, 0);
    drawTexturedBoxGeometry(0.2, 5.2, 0.2, metalTextureID);
    glPopMatrix();
    //Top
    glPushMatrix();
    glTranslatef(3, 0, 2);
    glRotatef(-15, 1, 0, 0);
    drawTexturedBoxGeometry(0.2, 5.2, 0.2, metalTextureID);
    glTranslatef(-6, 0, 0);
    drawTexturedBoxGeometry(0.2, 5.2, 0.2, metalTextureID);
    glPopMatrix();
    glPushMatrix();
    glTranslatef(-3, 5, 0.65);
    glRotatef(-90, 0, 0, 1);
    drawTexturedBoxGeometry(0.2, 6, 0.2, metalTextureID);
    glPopMatrix();
    glPopMatrix();
    glPushMatrix();
    glTranslatef(0, 5, 0.6);
    //Swing
    glPushMatrix();
    if (onOffSwitch) {
        glRotatef(-swingAngle, 0, 0, 1);
    }
    glPushMatrix();
    glTranslatef(0.5, -3.5, 0);
    glTranslatef(-1, 0, 0);
    drawTexturedCylinderGeometry(0.05, 3.5, 70, ropeTextureID);
    glPopMatrix();
    glPushMatrix();
    glPushMatrix();
    glTranslatef(-0.5, -3.6, 0);
    drawTexturedBoxGeometry(3.5, 0.2, 1, swingTextureID);
    glPopMatrix();
    glPopMatrix();
    if (onOffSwitch) {
        glRotatef(swingAngle, 0, 0, 1);
    }
    glPopMatrix();
    glPopMatrix();
    glEnable(GL_TEXTURE_2D);
    glPopMatrix();
    glDisable(GL_LIGHTING);

}

void drawPoolBalls(float height, float width, float length) {

    glPushMatrix();
    glDisable(GL_TEXTURE_2D);
    drawTexturedBoxGeometry(width, height, length, ballspoolTextureID);
    glTranslatef(width / 2 + 0.05, 0, 0);
    drawTexturedBoxGeometry(0.2, height + 0.5, length, redRubberTextureID);
    glTranslatef(-2 * (width / 2 + 0.05), 0, 0);
    drawTexturedBoxGeometry(0.2, height + 0.5, length, redRubberTextureID);
    glTranslatef(width / 2 + 0.15, 0, 0);
    glTranslatef(-0.1, 0, length / 2 + 0.1);
    drawTexturedBoxGeometry(width + 0.3, height + 0.5, 0.2, redRubberTextureID);
    glTranslatef(0, 0, -2 * (length / 2 + 0.1));
    drawTexturedBoxGeometry(width + 0.3, height + 0.5, 0.2, redRubberTextureID);
    glEnable(GL_TEXTURE_2D);
    glPopMatrix();

}

void drawPool(float height, float width, float length) {

    glPushMatrix();
    glDisable(GL_TEXTURE_2D);
    drawTexturedBoxGeometry(width, height, length, poolTextureID);
    glTranslatef(width / 2 + 0.05, 0, 0);
    drawTexturedBoxGeometry(0.2, height + 0.5, length, woodTextureID);
    glTranslatef(-2 * (width / 2 + 0.05), 0, 0);
    drawTexturedBoxGeometry(0.2, height + 0.5, length, woodTextureID);
    glTranslatef(width / 2 + 0.15, 0, 0);
    glTranslatef(-0.1, 0, length / 2 + 0.1);
    drawTexturedBoxGeometry(width + 0.3, height + 0.5, 0.2, woodTextureID);
    glTranslatef(0, 0, -2 * (length / 2 + 0.1));
    drawTexturedBoxGeometry(width + 0.3, height + 0.5, 0.2, woodTextureID);
    glEnable(GL_TEXTURE_2D);
    glPopMatrix();

}

void drawSlide(float height) {
    glPushMatrix();
    glDisable(GL_LIGHTING);
    glDisable(GL_TEXTURE_2D);

    //Sliding part
    glPushMatrix();
    glTranslatef(0, 3.5, 0.65);
    glColor3f(1, 0, 0);
    drawTexturedBoxGeometry(1, 0.1, 1.5, slideTextureID);
    glPopMatrix();
    glPushMatrix();
    glTranslatef(0.4, 0, 1.3);
    glColor3f(0, 0, 0);
    drawTexturedCylinderGeometry(0.1, 3.5, 70, slideTextureID);
    glTranslatef(-0.8, 0, 0);
    drawTexturedCylinderGeometry(0.1, 3.5, 70, slideTextureID);
    glPopMatrix();
    glPushMatrix();
    glRotatef(45, 1, 0, 0);
    glTranslatef(0, 3.4, 0.6);
    glColor3f(1, 0, 0);
    drawTexturedBoxGeometry(1, 0.1, 4.5, slideTextureID);
    glColor3f(1, 0, 0);
    glPopMatrix();
    glPushMatrix();
    glTranslatef(0, 0.4, 5.1);
    glColor3f(1, 0, 0);
    drawTexturedBoxGeometry(1, 0.1, 1.5, slideTextureID);
    glPopMatrix();
    glPushMatrix();
    glTranslatef(0.4, 0, 4.4);
    glColor3f(0, 0, 0);
    drawTexturedCylinderGeometry(0.1, 0.5, 70, slideTextureID);
    glTranslated(-0.8, 0, 0);
    drawTexturedCylinderGeometry(0.1, 0.5, 70, slideTextureID);
    glTranslatef(0, 0, 1.3);
    drawTexturedCylinderGeometry(0.1, 0.5, 70, slideTextureID);
    glTranslatef(0.8, 0, 0);
    drawTexturedCylinderGeometry(0.1, 0.5, 70, slideTextureID);
    glPopMatrix();

    //Handles
    glColor3f(0.5, 0.5, 0.5);
    glPushMatrix();
    glColor3f(1, 1, 1);
    glTranslatef(-0.5, 3.6, 0.7);
    drawTexturedBoxGeometry(0.1, 0.5, 1.5, redRubberTextureID);
    glTranslatef(1, 0, 0);
    drawTexturedBoxGeometry(0.1, 0.5, 1.5, redRubberTextureID);
    glPopMatrix();
    glPushMatrix();
    glTranslatef(-0.5, 0.5, 5.1);
    drawTexturedBoxGeometry(0.1, 0.5, 1.5, redRubberTextureID);
    glTranslatef(1, 0, 0);
    drawTexturedBoxGeometry(0.1, 0.5, 1.5, redRubberTextureID);
    glPopMatrix();
    glPushMatrix();
    glTranslatef(-0.5, 2.1, 2.7);
    glRotatef(45, 1, 0, 0);
    drawTexturedBoxGeometry(0.1, 0.5, 4.5, redRubberTextureID);
    glPopMatrix();
    glPushMatrix();
    glTranslatef(0.5, 2.1, 2.7);
    glRotatef(45, 1, 0, 0);
    drawTexturedBoxGeometry(0.1, 0.5, 4.5, redRubberTextureID);
    glPopMatrix();

    //Ladder
    glPushMatrix();
    glColor3f(0, 0, 0);
    glTranslatef(0.5, 0, 0);
    drawTexturedCylinderGeometry(0.1, 4.2, 70, metal2TextureID);
    glPopMatrix();
    glPushMatrix();
    glTranslatef(-0.5, 0, 0);
    drawTexturedCylinderGeometry(0.1, 4.2, 70, metal2TextureID);
    glPopMatrix();
    glPushMatrix();
    glTranslatef(0.5, 0.5, 0);
    glRotatef(90, 0, 0, 1);
    drawTexturedCylinderGeometry(0.1, 1, 70, metal2TextureID);
    glTranslatef(0.5, 0, 0);
    drawTexturedCylinderGeometry(0.1, 1, 70, metal2TextureID);
    glTranslatef(0.5, 0, 0);
    drawTexturedCylinderGeometry(0.1, 1, 70, metal2TextureID);
    glTranslatef(0.5, 0, 0);
    drawTexturedCylinderGeometry(0.1, 1, 70, metal2TextureID);
    glTranslatef(0.5, 0, 0);
    drawTexturedCylinderGeometry(0.1, 1, 70, metal2TextureID);
    glTranslatef(0.5, 0, 0);
    drawTexturedCylinderGeometry(0.1, 1, 70, metal2TextureID);
    glPopMatrix();
    glPushMatrix();
    glTranslatef(0, 0, 5.7);
    drawPoolBalls(0.2, 3, 4);
    glPopMatrix();
     glEnable(GL_LIGHTING);
    glEnable(GL_TEXTURE_2D);
    glPopMatrix();
}

void drawFerrisWheel(int size, int numCabins) {
    glPushMatrix(); // Save the current matrix state
    glPushMatrix(); // Save the current matrix state
    glTranslatef(0, 0, -0.3); // Translate to the base position and raise the cylinder height
    // Draw the cylindrical base of the Ferris wheel
    drawTexturedCylinderGeometry(0.3f, size * 2.5, 70, metal2TextureID); // Increase the cylinder height
    glPopMatrix(); // Restore the matrix state

    glEnable(GL_LIGHTING);

    glTranslatef(0, size * 2, 0); // Translate to the sphere position
    // Draw the spherical top of the Ferris wheel
    glutSolidSphere(0.25, 10, 15);

    if (onOffSwitch) {
        glRotatef(vaneRotationAngle, 0, 0, 1); // Rotate the Ferris wheel if the switch is on
    }
    glScalef(size, size, size); // Scale the Ferris wheel

     // Material Properties
    GLfloat mat_ambient[] = { 0.2, 0.2, 0.2, 1.0 };
    GLfloat mat_diffuse[] = { 0.8, 0.8, 0.8, 1.0 };
    GLfloat mat_specular[] = { 1.0, 1.0, 1.0, 1.0 };
    GLfloat mat_shininess[] = { 100.0 };

    glMaterialfv(GL_FRONT, GL_AMBIENT, mat_ambient);
    glMaterialfv(GL_FRONT, GL_DIFFUSE, mat_diffuse);
    glMaterialfv(GL_FRONT, GL_SPECULAR, mat_specular);
    glMaterialfv(GL_FRONT, GL_SHININESS, mat_shininess);

    // Draw Ferris wheel structure
    glTranslatef(0, 0, 0.15);
    // Draw the central hub
    glutSolidSphere(0.05, 10, 10);

    // Draw the spokes
    glLineWidth(3.0); // Set the line width to bold
    glBegin(GL_LINES);
    for (int i = 0; i < 360; i += 360 / numCabins) {
        float angle = i * Pi / 180.0; // Convert angle to radians
        glVertex3f(0, 0, 0); // Starting point (center)
        glVertex3f(1.25 * sin(angle), 1.25 * cos(angle), 0); // End point (on the edge of the wheel)
    }
    glEnd();

    glLineWidth(1.0); // Reset line width
    // Draw the torus
    glutSolidTorus(0.03, size * 0.6, 10, 15); // Increase the torus radius


     // Material Properties
    GLfloat mat_ambien[] = { 0.2, 0.2, 0.2, 1.0 };
    GLfloat mat_diffus[] = { 0.8, 0.8, 0.8, 1.0 };
    GLfloat mat_specula[] = { 1.0, 1.0, 1.0, 1.0 };
    GLfloat mat_shinines[] = { 100.0 };

    glMaterialfv(GL_FRONT, GL_AMBIENT, mat_ambien);
    glMaterialfv(GL_FRONT, GL_DIFFUSE, mat_diffus);
    glMaterialfv(GL_FRONT, GL_SPECULAR, mat_specula);
    glMaterialfv(GL_FRONT, GL_SHININESS, mat_shinines);

    // Draw the cabins
    for (int i = 0; i < numCabins; ++i) {
        float angle = 360.0f / numCabins * i; // Calculate angle for each cabin
        float torusRadius = size * 0.6;
        float cabinAngle = 2 * Pi * i / numCabins;
        float cabinX = (torusRadius + 0.1) * cos(cabinAngle); // Increase the radius and raise the height of the cabin
        float cabinY = (torusRadius + 0.1) * sin(cabinAngle); // Increase the radius and raise the height of the cabin
        float cabinZ = 0;
        glPushMatrix();
        glTranslatef(cabinX, cabinY, cabinZ);
        glRotatef(angle, 0, 0, 1); // Rotate to the current angle
        // Draw the cabin (a simple rectangular box)
        drawTexturedBoxGeometry(0.15, 0.15, 0.15, cabinTextureID);
        glPopMatrix();
    }

    glLineWidth(1.0); // Reset line width
    glPopMatrix(); // Restore the matrix state

    // Disable texture mapping
    glDisable(GL_TEXTURE_2D);
    glDisable(GL_LIGHTING);
}

void drawBench() {
    //draw bench frame
    glPushMatrix();

    drawTexturedBoxGeometry(0.1, 1, 0.1, metalTextureID);
    glTranslatef(2, 0, 0);
    drawTexturedBoxGeometry(0.1, 1, 0.1, metalTextureID);
    glTranslatef(0, 0, -1);
    drawTexturedBoxGeometry(0.1, 1, 0.1, metalTextureID);
    glTranslatef(-2, 0, 0);
    drawTexturedBoxGeometry(0.1, 1, 0.1, metalTextureID);
    glPopMatrix();

    glPushMatrix();
    glTranslatef(2, 1, 0);
    glRotatef(90, 0, 0, 1);
    drawTexturedBoxGeometry(0.1, 2, 0.1, metalTextureID);
    glTranslatef(0, 0, -1);
    drawTexturedBoxGeometry(0.1, 2, 0.1, metalTextureID);
    glPopMatrix();

    glPushMatrix();
    glTranslatef(2, 1, -1);
    glRotatef(90, 1, 0, 0);
    drawTexturedBoxGeometry(0.1, 1, 0.1, metalTextureID);
    glTranslatef(-2, 0, 0);
    drawTexturedBoxGeometry(0.1, 1, 0.1, metalTextureID);
    glPopMatrix();

    glPushMatrix();

    glTranslatef(0, 1, -1);
    glRotatef(-20, 1, 0, 0);
    drawTexturedBoxGeometry(0.1, 1, 0.1, metalTextureID);
    glRotatef(20, 1, 0, 0);
    glTranslatef(2, 0, 0);
    glRotatef(-20, 1, 0, 0);
    drawTexturedBoxGeometry(0.1, 1, 0.1, metalTextureID);
    glPopMatrix();

    //draw planks
    glPushMatrix();
    glTranslatef(1, 1, 0);
    drawTexturedBoxGeometry(2.7, 0.1, 0.25, woodTextureID);
    glTranslatef(0, 0, -0.3);
    drawTexturedBoxGeometry(2.7, 0.1, 0.25, woodTextureID);
    glTranslatef(0, 0, -0.3);
    drawTexturedBoxGeometry(2.7, 0.1, 0.25, woodTextureID);
    glTranslatef(0, 0, -0.3);
    drawTexturedBoxGeometry(2.7, 0.1, 0.25, woodTextureID);
    glPopMatrix();

    glPushMatrix();
    glTranslatef(1, 1.3, -1.05);
    glRotatef(70, 1, 0, 0);
    drawTexturedBoxGeometry(2.7, 0.1, 0.25, woodTextureID);
    glTranslatef(0, 0, -0.3);
    drawTexturedBoxGeometry(2.7, 0.1, 0.25, woodTextureID);
    glTranslatef(0, 0, -0.3);
    drawTexturedBoxGeometry(2.7, 0.1, 0.25, woodTextureID);

    glPopMatrix();
    glEnable(GL_TEXTURE_2D);
}

// Function to draw a textured trash can
void drawTrashCan(float width, float height, float depth) {
    // Draw the main body of the trash can
    drawTexturedBoxGeometry(width, height, depth, metalTextureID);

    // Draw the lid of the trash can
    float lidWidth = width * 0.8f;
    float lidDepth = depth * 0.8f;
    float lidHeight = height * 0.1f; // Assuming the lid is 10% of the height
    float lidOffset = height + lidHeight * 0.5f; // Offset to position the lid on top of the trash can

    // Enable texture 2D
    glEnable(GL_TEXTURE_2D);

    // Bind the same texture for the lid
    glBindTexture(GL_TEXTURE_2D, trashTextureID);

    glPushMatrix();
    glTranslatef(0, -0.001,0);


    // Draw the lid
    glBegin(GL_QUADS);
    // Top face of the lid
    glTexCoord2f(0, 0); glVertex3f(-lidWidth / 2, lidOffset, lidDepth / 2);
    glTexCoord2f(1, 0); glVertex3f(lidWidth / 2, lidOffset, lidDepth / 2);
    glTexCoord2f(1, 1); glVertex3f(lidWidth / 2, lidOffset, -lidDepth / 2);
    glTexCoord2f(0, 1); glVertex3f(-lidWidth / 2, lidOffset, -lidDepth / 2);
    glEnd();
    glPopMatrix();
    // Disable texture 2D
    glDisable(GL_TEXTURE_2D);
}

void drawStreetLamp() {
    // Define dimensions for the lamp post (cylinder) and the lamp fixture (box)
    float cylinderRadius = 0.1f;
    float cylinderHeight = 2.5f;
    int cylinderSlices = 20;
    float boxWidth = 0.3f;
    float boxHeight = 0.3f;
    float boxDepth = 0.3f;

    // Define material properties
    GLfloat lampPostColor[] = {0.5f, 0.5f, 0.5f, 1.0f}; // Gray color for lamp post
    GLfloat lampFixtureColor[] = {0.9f, 0.9f, 0.1f, 1.0f}; // Yellowish color for lamp fixture

    // Draw lamp post (cylinder)
    glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, lampPostColor); // Set material properties for lamp post
    drawTexturedCylinderGeometry(cylinderRadius, cylinderHeight, cylinderSlices, metalTextureID); // Draw cylinder

    // Draw lamp fixture (box)
    glPushMatrix();
    glTranslatef(0.0f, cylinderHeight, 0.0f); // Move to top of lamp post
    glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, lampFixtureColor); // Set material properties for lamp fixture
    drawTexturedBoxGeometry(boxWidth, boxHeight, boxDepth, 0); // Draw box without texture
    glPopMatrix();
}

void drawAllLampsBanchTrashs() {
    glPushMatrix();
    glTranslatef(15, 0, 0);
    glRotatef(-90, 0, 1, 0);
    glScalef(1.5, 1.5, 1.5);
    drawBench();
    glPopMatrix();
    glPushMatrix();
    glTranslatef(15, 0, -2);
    glScalef(2, 2, 2);
    drawStreetLamp();
    glPopMatrix();
    glPushMatrix();
    glTranslatef(15, 0, 19);
    glScalef(2, 2, 2);
    drawStreetLamp();
    glPopMatrix();



    glPushMatrix();
    glTranslatef(-18, 0, 0);
    glScalef(2, 2, 2);
    drawStreetLamp();
    glPopMatrix();
    glPushMatrix();
    glTranslatef(-18, 0, 10);
    glScalef(2, 2, 2);
    drawStreetLamp();
    glPopMatrix();
    glPushMatrix();
    glTranslatef(-18, 0, 8);
    drawTrashCan(1, 2, 1);
    glPopMatrix();
    glPushMatrix();
    glTranslatef(-18, 0, 5.5);
    glScalef(1.5, 1.5, 1.5);
    glRotatef(90, 0, 1, 0);
    drawBench();
    glPopMatrix();

    glPushMatrix();
    glTranslatef(15, 0, 6);
    drawTrashCan(1, 2, 1);
    glPopMatrix();
    glPushMatrix();
    glTranslatef(15, 0, 8);
    glRotatef(-90, 0, 1, 0);
    glScalef(1.5, 1.5, 1.5);
    drawBench();
    glPopMatrix();
    glPushMatrix();
    glTranslatef(15, 0, 13);
    drawTrashCan(1, 2, 1);
    glPopMatrix();
    glPushMatrix();
    glTranslatef(15, 0, 15);
    glRotatef(-90, 0, 1, 0);
    glScalef(1.5, 1.5, 1.5);
    drawBench();
    glPopMatrix();
    glPushMatrix();

    glPushMatrix();
    glTranslatef(6, 0, -15);
    glScalef(2, 2, 2);
    drawStreetLamp();
    glPopMatrix();
    glTranslatef(0, 0, -15);
    glScalef(1.5, 1.5, 1.5);
    drawBench();
    glPopMatrix();
    glPushMatrix();
    glTranslatef(-2, 0, -16);
    drawTrashCan(1, 2, 1);
    glPopMatrix();

    glPushMatrix();
    glTranslatef(-8, 0, -15);
    glScalef(1.5, 1.5, 1.5);
    drawBench();
    glPopMatrix();
    glPushMatrix();
    glTranslatef(-10, 0, -16);
    glScalef(2, 2, 2);
    drawStreetLamp();
    glPopMatrix();
    glPushMatrix();
    glTranslatef(-12, 0, -16);
    drawTrashCan(1, 2, 1);
    glPopMatrix();
}
void handleTerminationError(char* ErrorString)
{
    char string[256];
    printf(ErrorString);
    fgets(string, 256, stdin);

    exit(1);
}
// Function to load bmp file
// buffer for the image is allocated in this function, you should free this buffer
GLubyte* readBMPImage(char* imagepath, int* width, int* height)
{
	unsigned char header[54]; // Each BMP file begins by a 54-bytes header
	unsigned int dataPos;     // Position in the file where the actual data begins
	unsigned int imageSize;   // = width*height*3
	unsigned char* data;
	unsigned char tmp;
	int i;

	// Open the file
	FILE* file = fopen(imagepath, "rb");
	if (!file) {
		handleTerminationError("Image could not be opened\n");
	}

	if (fread(header, 1, 54, file) != 54) { // If not 54 bytes read : problem
		handleTerminationError("Not a correct BMP file\n");
	}

	if (header[0] != 'B' || header[1] != 'M') {
		handleTerminationError("Not a correct BMP file\n");
	}

	// Read ints from the byte array
	dataPos = *(int*)&(header[0x0A]);
	imageSize = *(int*)&(header[0x22]);
	*width = *(int*)&(header[0x12]);
	*height = *(int*)&(header[0x16]);

	// Some BMP files are misformatted, guess missing information
	if (imageSize == 0)
		imageSize = *width * *height * 3; // 3 : one byte for each Red, Green and Blue component
	if (dataPos == 0)
		dataPos = 54; // The BMP header is done that way

	// Create a buffer
	data = malloc(imageSize * sizeof(GLubyte));

	// Read the actual data from the file into the buffer
	fread(data, 1, imageSize, file);


	//swap the r and b values to get RGB (bitmap is BGR)
	for (i = 0; i < *width * *height; i++)
	{
		tmp = data[i * 3];
		data[i * 3] = data[i * 3 + 2];
		data[i * 3 + 2] = tmp;
	}


	//Everything is in memory now, the file can be closed
	fclose(file);

	return data;
}