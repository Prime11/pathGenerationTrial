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
			i = 0;
		} else {
			i = 1;
		}
		while (widthY >= 0) {
			if (i % 2 == 0) {
				for (int j = (int) widthX; j >= 0; j--) {
					generatedStack.push(new Point((j), Math.abs(currentY
							- widthY), false));
				}
			} else {
				for (int j = 0; j <= (int) widthX; j++) {
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
		Stack<Point> generatedStack = firstStack;
		Stack<Point> backpedalStack = new Stack<Point>();
		double currentX = startPointX;
		double currentY = startPointY;
		double displacementX = (endPointX - currentX);
		double displacementY = (endPointY - currentY);
		Point endPoint = new Point(endPointX, endPointY, false);
		if (!isDangerous(endPoint, dangerPointsL)) {
			boolean doneX = false, doneY = false;
			while (!doneX && !doneY) {
				if (!doneX) {
					Point nextP = new Point((currentX + displacementX
							/ Math.abs(displacementX)), currentY, false);
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
					Point nextP = new Point(currentX, currentY + displacementY
							/ Math.abs(displacementY), false);
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
				if (generatedStack.isEmpty()
						|| !Point.areSamePoints(backpedalStack.peek(),
								generatedStack.peek())) {
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
				currentStack.pop();
				Point nextP = currentStack.peek();
				if (isDangerous(Point.upAdjacentPoint(backPedalStack.peek()),
						dangerousPointsL)
						|| isDangerous(
								Point.downAdjacentPoint(backPedalStack.peek()),
								dangerousPointsL)) {
					if (backPedalStack.peek().x <= (redX1 + redX2) / 2) {
						System.out.print("\t"
								+ Point.upAdjacentPoint(backPedalStack.peek())
										.pointToTV());
						System.out
								.print("\t"
										+ Point.downAdjacentPoint(
												backPedalStack.peek())
												.pointToTV());
						currentStack.push(Point
								.leftAdjacentPoint(backPedalStack.peek()));
						backPedalStack.push(Point
								.leftAdjacentPoint(backPedalStack.peek()));
					} else {
						System.out.print("\t"
								+ Point.upAdjacentPoint(backPedalStack.peek())
										.pointToTV());
						System.out
								.print("\t"
										+ Point.downAdjacentPoint(
												backPedalStack.peek())
												.pointToTV());
						currentStack.push(Point
								.rightAdjacentPoint(backPedalStack.peek()));
						backPedalStack.push(Point
								.rightAdjacentPoint(backPedalStack.peek()));
					}
				} else if (isDangerous(
						Point.rightAdjacentPoint(backPedalStack.peek()),
						dangerousPointsL)
						|| isDangerous(
								Point.leftAdjacentPoint(backPedalStack.peek()),
								dangerousPointsL)) {
					if (backPedalStack.peek().y < (redY1 + redY2) / 2) {
						System.out
								.print("\t"
										+ Point.leftAdjacentPoint(
												backPedalStack.peek())
												.pointToTV());
						System.out.print("\t"
								+ Point.rightAdjacentPoint(
										backPedalStack.peek()).pointToTV());
						currentStack.push(Point
								.downAdjacentPoint(backPedalStack.peek()));
						backPedalStack.push(Point
								.downAdjacentPoint(backPedalStack.peek()));
					} else {
						System.out
								.print("\t"
										+ Point.leftAdjacentPoint(
												backPedalStack.peek())
												.pointToTV());
						System.out.print("\t"
								+ Point.rightAdjacentPoint(
										backPedalStack.peek()).pointToTV());
						currentStack.push(Point.upAdjacentPoint(backPedalStack
								.peek()));
						backPedalStack.push(Point
								.upAdjacentPoint(backPedalStack.peek()));
					}
				}
				currentStack = generatePath(false, o.x, o.y, nextP.x, nextP.y,
						dangerousPointsL, currentStack);
				// currentStack.push(backPedalStack.pop());

			}
		}
	}

	public static void main(String[] args) {
		Stack<Point> emptyStack = new Stack<Point>();
		Stack<Point> backPedalStack = new Stack<Point>();
		double finishX = 2, finishY = 9;
		double redX1 = 5, redY1 = 5;
		double redX2 = 7, redY2 = 7;
		double startX = 10, startY = 0;
		int widthX = 10, widthY = 10;
		ArrayList<Point> dangerousPointsL = new ArrayList<Point>();
		insertDangerList(redX1, redY1, redX2, redY2, dangerousPointsL);
		emptyStack = generateSetPath(false, startX, startY, startX, startY,
				widthX, widthY, dangerousPointsL, emptyStack);
		runSimpleCourse(finishX, finishY, widthX, widthY, redX1, redX2, redY1,
				redY2, dangerousPointsL, emptyStack, backPedalStack);

	}
}
