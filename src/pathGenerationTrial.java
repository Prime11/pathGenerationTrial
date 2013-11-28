import java.util.ListIterator;
import java.util.Stack;
import java.util.ArrayList;

public class pathGenerationTrial {

	public static void insertDangerList(double redX1, double redY1,
			double redX2, double redY2, ArrayList<Point> dangerPointsL) {
		int stepsX = (int) Math.round(Math.abs(redX2 - redX1));
		int stepsY = (int) Math.round(Math.abs(redY2 - redY1));
		for (int i = 0; i <= stepsX; i++) {
			for (int j = 0; j <= stepsY; j++) {
				dangerPointsL.add(new Point((redX1 + i), (redY1 + j), false));
			}
		}
	}

	private static boolean isDangerous(Point currentPoint,
			ArrayList<Point> dangerousPoints) {
		ListIterator<Point> iter = dangerousPoints.listIterator();
		Point nextPoint;
		while (iter.hasNext()) {
			nextPoint = iter.next();
			if (Point.areSamePoints(currentPoint, nextPoint)) {
				return true;
			}
		}
		return false;
	}

	public static Stack<Point> generateSetPath(boolean finish,
			double startPointX, double startPointY, double endPointX,
			double endPointY, double widthX, double widthY,
			ArrayList<Point> dangerPointsL, Stack<Point> firstStack) {
		Stack<Point> generatedStack = firstStack;
		double currentX = startPointX;
		double currentY = startPointY;
		int i;
		if (currentX < (widthX) / 2) {
			i = 1;
		} else {
			i = 0;
		}
		while (widthY >= 1) {
			if (i % 2 == 0) {
				for (int j = (int) widthX; j >= 1; j--) {
					generatedStack.push(new Point((j), Math.abs(currentY
							- widthY), false));
				}
			} else {
				for (int j = 1; j <= (int) widthX; j++) {
					generatedStack.push(new Point((j), Math.abs(currentY
							- widthY), false));
				}
			}
			widthY--;
			i++;

		}
		return generatedStack;
	}

	public static Stack<Point> generatePath(boolean finish, double startPointX,
			double startPointY, double endPointX, double endPointY,
			ArrayList<Point> dangerPointsL, Stack<Point> firstStack) {
		//System.out.println("inside generatePath");
		Stack<Point> generatedStack = firstStack;
		Stack<Point> backpedalStack = new Stack<Point>();
		double currentX = startPointX;
		double currentY = startPointY;
		double displacementX = (endPointX - currentX);
		double displacementY = (endPointY - currentY);
		Point endPoint = new Point(endPointX, endPointY, false);
		//System.out.println("got here");
		//System.out.println("Endpoint: " + endPoint.pointToTV());
		if (!isDangerous(endPoint, dangerPointsL)) {
			//System.out.println("inside if");
			boolean doneX = false, doneY = false;
			while (!doneX && !doneY) {
				//System.out.println("inside while");
				if (!doneX) {
					Point nextP = new Point((currentX + displacementX / Math.abs(displacementX)), currentY, false);
					if (!isDangerous(nextP, dangerPointsL)) {
						if (endPointX != (currentX)) {
							currentX += displacementX / Math.abs(displacementX);
							if (finish) {
								generatedStack.push(nextP);
							}
							backpedalStack.push(nextP);
						} else {
							doneX = true;
						}
					}
				}
				if (!doneY) {
					Point nextP = new Point(currentX, currentY + displacementY / Math.abs(displacementY), false);
					if (!isDangerous(nextP, dangerPointsL)) {
						if (endPointY != currentY) {
							currentY += displacementY / Math.abs(displacementY);
							if (finish) {
								generatedStack.push(nextP);
							}
							backpedalStack.push(nextP);
						} else {
							doneY = true;
						}
					}
				}
			}
			while (!backpedalStack.empty()) {
				if (generatedStack.isEmpty() || !Point.areSamePoints(backpedalStack.peek(), generatedStack.peek())) {
					generatedStack.push(backpedalStack.pop());
				} else {
					backpedalStack.pop();
				}
			}
		}

		return generatedStack;
	}

	public static void runSimpleCourse(double finishX, double finishY,
			double widthX, double widthY, double redX1, double redX2,
			double redY1, double redY2, ArrayList<Point> dangerousPointsL,
			Stack<Point> currentStack, Stack<Point> backPedalStack) {
		Point o = new Point();
		while (!currentStack.isEmpty()) {
			if (!isDangerous(currentStack.peek(), dangerousPointsL)
					&& !currentStack.peek().outOfBounds(widthX, widthY)) {
				currentStack.peek().setVisited();
				System.out.print("\n" + currentStack.peek().pointToTV());
				o = currentStack.peek();
				backPedalStack.push(currentStack.pop());
			} else {
				System.out.print("\t" + currentStack.peek().pointToTV());
				currentStack.pop();
				if (!currentStack.isEmpty()){
					Point nextP = currentStack.peek();
					if (isDangerous(Point.upAdjacentPoint(backPedalStack.peek()), dangerousPointsL)
							|| isDangerous(Point.downAdjacentPoint(backPedalStack.peek()), dangerousPointsL)) {
						if (backPedalStack.peek().x <= (redX1 + redX2) / 2 && !Point.leftAdjacentPoint(backPedalStack.peek()).outOfBounds(widthX, widthY)) {
							System.out.print("\t" + currentStack.peek().pointToTV());
							currentStack.push(Point.leftAdjacentPoint(backPedalStack.peek()));
							backPedalStack.push(Point.leftAdjacentPoint(backPedalStack.peek()));
						} else if (!Point.rightAdjacentPoint(backPedalStack.peek()).outOfBounds(widthX, widthY)) {
							System.out.print("\t" + currentStack.peek().pointToTV());
							currentStack.push(Point.rightAdjacentPoint(backPedalStack.peek()));
							backPedalStack.push(Point.rightAdjacentPoint(backPedalStack.peek()));
						}
					} else if (isDangerous(Point.rightAdjacentPoint(backPedalStack.peek()), dangerousPointsL)
							|| isDangerous(Point.leftAdjacentPoint(backPedalStack.peek()), dangerousPointsL)) {
						if (backPedalStack.peek().y <= (redY1 + redY2) / 2 && !Point.downAdjacentPoint(backPedalStack.peek()).outOfBounds(widthX, widthY)) {
							System.out.print("\t" + currentStack.peek().pointToTV());
							currentStack.push(Point.downAdjacentPoint(backPedalStack.peek()));
							backPedalStack.push(Point.downAdjacentPoint(backPedalStack.peek()));
						} else if(!Point.upAdjacentPoint(backPedalStack.peek()).outOfBounds(widthX, widthY)) {
							System.out.print("\t" + currentStack.peek().pointToTV());
							currentStack.push(Point.upAdjacentPoint(backPedalStack.peek()));
							backPedalStack.push(Point.upAdjacentPoint(backPedalStack.peek()));
						}
					}
					currentStack = generatePath(false, o.x, o.y, nextP.x, nextP.y,
							dangerousPointsL, currentStack);
				}
			}
		}
	}

	public static void main(String[] args) {
		Stack<Point> emptyStack = new Stack<Point>();
		Stack<Point> backPedalStack = new Stack<Point>();
		double finishX = 4, finishY = 6;
		double redX1 = 2, redY1 = 5;
		double redX2 = 3, redY2 = 7;
		double greenX1 = 4, greenY1 = 5;
		double greenX2 = 6, greenY2 = 7;
		double startX = 0, startY = 0;
		int widthX = 10, widthY = 10;
		ArrayList<Point> dangerousPointsL = new ArrayList<Point>();
		insertDangerList(redX1, redY1, redX2, redY2, dangerousPointsL);
		insertDangerList(0, widthY, 0, widthY, dangerousPointsL);
		insertDangerList(widthX,widthY, widthX, widthY, dangerousPointsL);
		insertDangerList(widthX, 0, widthX, 0, dangerousPointsL);
		//insertDangerList(greenX1, greenY1, greenX2, greenY2, dangerousPointsL);
		emptyStack = generateSetPath(false, startX, startY, startX, startY,
				widthX, widthY, dangerousPointsL, emptyStack);
		backPedalStack.push(new Point(startX, startY, false));
		//emptyStack = generatePath(false, startX, startY, finishX, finishY, dangerousPointsL, emptyStack);
		runSimpleCourse(finishX, finishY, widthX, widthY, redX1, redX2, redY1,
				redY2, dangerousPointsL, emptyStack, backPedalStack);

	}
}
