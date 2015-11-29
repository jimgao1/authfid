// Raspberry Pi 7-inch tft touch driver

#include <linux/i2c-dev.h>
#include <linux/ioctl.h>
#include <errno.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdio.h>

#include <X11/Xlib.h>
#include <X11/Xutil.h>

#define T_FILE "/dev/i2c-1"
#define T_ADDR 0x38

Display* display;

void init(void) {
	display = XOpenDisplay(NULL);
	if (display == NULL) {
		fprintf(stderr, "Can't open display");
		exit(-1);
	}
}

void mouseDown(void) {
	printf("Down\n");
	XEvent event;
	memset (&event, 0, sizeof (event));
	event.xbutton.button = 1;
	event.xbutton.same_screen = True;
	event.xbutton.subwindow = DefaultRootWindow (display);
	while (event.xbutton.subwindow) {
		event.xbutton.window = event.xbutton.subwindow;
		XQueryPointer (display, event.xbutton.window,
				 &event.xbutton.root, &event.xbutton.subwindow,
				 &event.xbutton.x_root, &event.xbutton.y_root,
				 &event.xbutton.x, &event.xbutton.y,
				 &event.xbutton.state);
	}
	event.type = ButtonPress;
	if (XSendEvent (display, PointerWindow, True, ButtonPressMask, &event) == 0)
		fprintf (stderr, "Error to send the event!\n");
	XFlush (display);
}

void mouseUp(void) {
	printf("Up\n");
	XEvent event;
	memset (&event, 0, sizeof (event));
	event.xbutton.button = 1;
	event.xbutton.same_screen = True;
	event.xbutton.subwindow = DefaultRootWindow (display);
	while (event.xbutton.subwindow) {
		event.xbutton.window = event.xbutton.subwindow;
		XQueryPointer (display, event.xbutton.window,
				 &event.xbutton.root, &event.xbutton.subwindow,
				 &event.xbutton.x_root, &event.xbutton.y_root,
				 &event.xbutton.x, &event.xbutton.y,
				 &event.xbutton.state);
	}
	event.type = ButtonRelease;
	if (XSendEvent (display, PointerWindow, True, ButtonReleaseMask, &event) == 0)
		fprintf (stderr, "Error to send the event!\n");
	XFlush (display);

}

void mouseLocation(int x, int y) {
	printf("Absolute\n");
	XEvent event;
	XQueryPointer (display, DefaultRootWindow (display),
				&event.xbutton.root, &event.xbutton.window,
				&event.xbutton.x_root, &event.xbutton.y_root,
				&event.xbutton.x, &event.xbutton.y,
				&event.xbutton.state);
	XWarpPointer(display, None, None, 0, 0, 0, 0, x - event.xbutton.x, y - event.xbutton.y);
	XFlush(display);
}

void mouseMove(int x, int y) {
	printf("Relative\n");
	XWarpPointer(display, None, None, 0, 0, 0, 0, x, y);
}

int main() {
	init();

	int file;
	if((file = open(T_FILE, O_RDWR)) < 0) {
		printf("E: open failed\n");
		return -1;
	}
	if(ioctl(file, I2C_SLAVE, T_ADDR) < 0) {
		printf("E: ioctl failed\n");
		printf("%d\n", errno);
		return -1;
	}

	int lastDown;
	int down;

	char buf[66];

	int lastX;
	int lastY;

	int x;
	int y;

	while(1) {
		write(file, (char[]){0x1}, 1);
		read(file, buf, 66);
		int npoints = buf[1];
		int ev = buf[2] >> 6;

		y = ((buf[2] & 0x7) << 8 | buf[3]);
		x = ((buf[4] & 0x7) << 8 | buf[5]);

		printf("%d %d\n", x, y);

		if(x != lastX || y != lastY) {
			mouseLocation(x, y);
		}

		lastX = x;
		lastY = y;

		down = ev == 2;

		if(down != lastDown) {
			if(down) {
				mouseLocation(x, y);
				lastX = x;
				lastY = y;
				mouseDown();
			}
			else {
				mouseUp();
			}
		}

		lastDown = down;
		
		

	}

	return 0;
}
