import java.util.ListIterator;
import java.util.Stack;
import java.util.ArrayList;

public class pathGenerationTrial {

	public static void createDangerList(double redX1, double redY1,
			double redX2, double redY2, ArrayList<Point> dangerPointsL) {
		int stepsX = (int) Math.round(Math.abs(redX2 - redX1));
		int stepsY = (int) Math.round(Math.abs(redY2 - redY1));
		for (int i = 0; i <= stepsX; i++) {
			for (int j = 0; j <= stepsY; j++) {
				dangerPointsL.add(new Point((redX1 + i), (redY1 + j), false));
				/*
				 * System.out.println(new Point((redX1 + i), (redY1 + j), false)
				 * .pointToString());
				 */
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
				// System.out.println(nextPoint.pointToString() +
				// " is dangerous.");
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
			// System.out.println(endPoint.pointToString() +
			// " is not dangerous");
			// for (int i = 0; i < stepsX + stepsY; i++) {
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
							/*
							 * System.out.println("\n" + nextP.pointToString() +
							 * " was pushed X");
							 */
						} else {
							doneX = true;
						}
					} else {
						/*
						 * System.out.println(" \n" +nextP.pointToString() +
						 * " is dangerous");
						 */}
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
							/*
							 * System.out.println("\n" + nextP.pointToString() +
							 * " was pushed Y");
							 */} else {
							doneY = true;
						}
					} else {
						/*
						 * System.out.println(nextP.pointToString() +
						 * " is dangerous");
						 */}
				}
			}
			while (!backpedalStack.empty()) {
				if (generatedStack.isEmpty()
						|| !Point.areSamePoints(backpedalStack.peek(),
								generatedStack.peek())) {
					generatedStack.push(backpedalStack.pop());
					/*
					 * System.out.println(generatedStack.peek().pointToString()
					 * + " pushed");
					 */} else {
					backpedalStack.pop();
				}
			}
		}

		return generatedStack;
	}

	public static void runSimpleCourse(double finishX, double finishY,
			double widthX, double widthY, double redX1, double redX2, double redY1, double redY2, ArrayList<Point> dangerousPointsL,
			Stack<Point> currentStack, Stack<Point> backPedalStack) {
		int i = 0, f = 0;
		Point o = new Point();
		while (!currentStack.isEmpty()) {
			if (!isDangerous(currentStack.peek(), dangerousPointsL) && !currentStack.peek().outOfBounds(widthX, widthY)){
				// LCD.drawInt((int) (10 * this.currentStack.peek().x), 0, 6);
				// LCD.drawInt((int) (10 * this.currentStack.peek().y), 6, 6);
				// currentStack = navi.travelTo(currentStack.peek().x,
				/*// currentStack.peek().y, 15, currentStack);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				currentStack.peek().setVisited();
				System.out.print("\n" + currentStack.peek().pointToTV());
				o = currentStack.peek();
				backPedalStack.push(currentStack.pop());
			} else {
				Point attemptedP = currentStack.pop();
				/*System.out.println(attemptedP.pointToString()
						+ " was attempted");
				*/Point nextP = currentStack.peek();
				//System.out.println(nextP.pointToString() + " is next Point");
				if (isDangerous(Point.upAdjacentPoint(backPedalStack.peek()), dangerousPointsL)
						|| isDangerous(Point.downAdjacentPoint(backPedalStack.peek()),
								dangerousPointsL)) {
					if (backPedalStack.peek().x <= (redX1 + redX2)/2){
					System.out.print("\t" + Point.upAdjacentPoint(backPedalStack.peek()).pointToTV());
					System.out.print("\t" + Point.downAdjacentPoint(backPedalStack.peek()).pointToTV());
					currentStack.push(Point.leftAdjacentPoint(backPedalStack.peek()));
					backPedalStack.push(Point.leftAdjacentPoint(backPedalStack.peek()));
					i++;
					}
					else {
						System.out.print("\t" + Point.upAdjacentPoint(backPedalStack.peek()).pointToTV());
						System.out.print("\t" + Point.downAdjacentPoint(backPedalStack.peek()).pointToTV());
						currentStack.push(Point.rightAdjacentPoint(backPedalStack.peek()));
						backPedalStack.push(Point.rightAdjacentPoint(backPedalStack.peek()));
						i++;
					}
					//o = backPedalStack.peek();
					//System.out.println("Tried X " + i + " time(s)");
				} else if (isDangerous(Point.rightAdjacentPoint(backPedalStack.peek()), dangerousPointsL)
						|| isDangerous(Point.leftAdjacentPoint(backPedalStack.peek()),
								dangerousPointsL)) {
					if (backPedalStack.peek().y <= (redY1 + redY2)/2){
					System.out.print("\t" + Point.leftAdjacentPoint(backPedalStack.peek()).pointToTV());
					System.out.print("\t" + Point.rightAdjacentPoint(backPedalStack.peek()).pointToTV());
					currentStack.push(Point.downAdjacentPoint(backPedalStack.peek()));
					backPedalStack.push(Point.downAdjacentPoint(backPedalStack.peek()));
					f++;
					}
					else {
						System.out.print("\t" + Point.leftAdjacentPoint(backPedalStack.peek()).pointToTV());
						System.out.print("\t" + Point.rightAdjacentPoint(backPedalStack.peek()).pointToTV());
						currentStack.push(Point.upAdjacentPoint(backPedalStack.peek()));
						backPedalStack.push(Point.upAdjacentPoint(backPedalStack.peek()));
					}
					//System.out.println("Tried Y " + f + " time(s)");
				}
/*				System.out.print("\nGenerating new path to "
						+ nextP.pointToTV());
						
*/				
				currentStack = generatePath(false, o.x, o.y, nextP.x,
						nextP.y, dangerousPointsL, currentStack);
				// currentStack.push(backPedalStack.pop());

			}
		}
	}

	public static void main(String[] args) {
		Stack<Point> emptyStack = new Stack<Point>();
		Stack<Point> backPedalStack = new Stack<Point>();
		double finishX = 2, finishY = 9;
		double redX1 = 0, redY1 = 2;
		double redX2 = 2, redY2 = 4;
		double startX = 0, startY = 0;
		int widthX = 10, widthY = 10;
		ArrayList<Point> dangerousPointsL = new ArrayList<Point>();
		createDangerList(redX1, redY1, redX2, redY2, dangerousPointsL);
		emptyStack = generateSetPath(false, startX, startY, startX, startY,
				widthX, widthY, dangerousPointsL, emptyStack);
		runSimpleCourse(finishX, finishY, widthX, widthY, redX1, redX2, redY1, redY2, dangerousPointsL,
				emptyStack, backPedalStack);

		System.out.print("\n---------------------");

	}
}
