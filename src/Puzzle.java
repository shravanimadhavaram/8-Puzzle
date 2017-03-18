import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Puzzle {
	
	private static final int[][] goalState=new int[3][3] ;
	static int i,j;

	public static void main(String[] args) {
		goalState[0][0]=1;
		goalState[0][1]=2;
		goalState[0][2]=3;
		goalState[1][0]=4;
		goalState[1][1]=5;
		goalState[1][2]=6;
		goalState[2][0]=7;
		goalState[2][1]=8;
		goalState[2][2]=0;
		Nodes mainNode= new Nodes();
		int[][] initialState = new int[3][3] ;
		System.out.println("Welcome to Bertie Woosters 8 puzzle solver.");
		while(true){
			System.out.println("Type 1 to use a default puzzle or 2 to enter your own puzzle or 3 to exit.");
			Scanner sc = new Scanner(System.in);
			int puzzle= sc.nextInt();
			while(puzzle<1 && puzzle >3 ){ // we can enter only 1 or 2 to select puzzle and 3 to exit
				System.out.println("Enter correct value for puzzle");
				System.out.println("Type 1 to use a default puzzle or 2 to enter your own puzzle or 3 to exit.");
				puzzle= sc.nextInt();
			}
			if(puzzle==1){// default puzzle
				initialState[0][0]=1;
				initialState[0][1]=8;
				initialState[0][2]=2;
				initialState[1][0]=0;
				initialState[1][1]=4;
				initialState[1][2]=3;
				initialState[2][0]=7;
				initialState[2][1]=6;
				initialState[2][2]=5;
			}
			else if(puzzle==2){// own puzzle
				for(i=0;i<3;i++){
					System.out.println("Enter "+(i+1)+" row use tabs or spaces between numbers");
					for(j=0;j<3;j++)
						initialState[i][j]=sc.nextInt();
				}
			}
			else if(puzzle==3)// to stop the loop
				break;
			if(validPuzzle(initialState)){// check whether puzzle is valid
				if(checkNotSolvable(initialState)){// check whether the puzzle is solvable
					PriorityQueue<Nodes> nodes = new PriorityQueue<Nodes>();
					mainNode.setValueH(0);
					mainNode.setValueG(0);
					mainNode.setPuzzle(initialState);
					System.out.println("Enter your choice of algorithm");
					System.out.println("1. Uniform Cost Search");
					System.out.println("2. A* with Misplaced Tile heuristic");
					System.out.println("3. A* with Manhattan Distance heuristic");
					int algo= sc.nextInt();
					nodes.offer(mainNode);
					generalSearch(nodes,algo);
				}
				else
					System.out.println("The 8 puzzle cannot be solved!");
			}
			else
				System.out.println("Enter valid values for puzzle!");
		}
	}
	
	//verifiying whether the entered matrix has elements from 0-8
	private static boolean validPuzzle(int[][] initialState) {
		int[] verify= new int[9];
		for(i=0;i<9;i++){
			verify[i]=0;
		}
		for(i=0;i<3;i++){
			for(j=0;j<3;j++){
				if(initialState[i][j]<9 && initialState[i][j]>=0)
					verify[initialState[i][j]]++;
			}
		}
		for(i=0;i<9;i++){
			if(verify[i]!=1)
				return false;
		}
		return true;
	}


	//this method is use to check whether the 8 puzzle is solvable or no.
	private static boolean checkNotSolvable(int[][] initialState) {
		int[] inversionList=new int[8];
		int count=0;
		for(i=0;i<3;i++){
            for(j=0;j<3;j++){
            	if(initialState[i][j]!=0){
            		inversionList[count]=initialState[i][j];
            		count++;
            	}
            }
        }
		count=0;
		for(i=0;i<8;i++)
			for(j=i+1;j<8;j++){
				if(inversionList[i]>inversionList[j]){
                    count++;
                }
			}
		if(count%2==1)
			return false;
		else
			return true;
		
	}

	
	//search algorithm 
	private static void generalSearch(PriorityQueue<Nodes> nodes, int algo) {
		List<int[][]> visitedNodes= new ArrayList<int[][]>();
		int countNodes=0,maxSize=0;
		while (nodes.peek()!=null) {
			System.out.println(nodes.size());
			if(nodes.size()>maxSize)
				maxSize=nodes.size();
			int c=0;
			Nodes temp= nodes.peek();
			nodes.remove();
			if(java.util.Arrays.deepEquals(goalState,temp.getPuzzle())){
				System.out.println("Goal!!");
				System.out.println("To solve this problem search algorithm expanded a total of "+countNodes);
				System.out.println("The maximum number of nodes in the queue at any one time was "+maxSize);
				System.out.println("Depth of the goal:" +temp.getValueG());
				break;
			}
			for(int[][] visited :visitedNodes ){//checking for repeated states
				if(java.util.Arrays.deepEquals(visited,temp.getPuzzle()))
					c++;
			}
			
			if(c==0){
				System.out.println("The best state to expand g(n)="+temp.getValueG()+"and h(n)=" +temp.getValueH()+" is:");
				for(int i=0;i<3;i++){
					System.out.println(temp.getPuzzle()[i][0] + " "+temp.getPuzzle()[i][1]+ " "+temp.getPuzzle()[i][2]);
				}
				countNodes++;
				Nodes left=moveleft(temp);// checking whether we can move left
				if(left.getValueG() != 0){
					if(algo==2)
						misplacedTile(left);
					else if (algo==3)
						manhattan(left);
					nodes.offer(left);
				}
				
				Nodes right=moveRight(temp);// checking whether we can move right
				if(right.getValueG() != 0){
					if(algo==2)
						misplacedTile(right);
					else if (algo==3)
						manhattan(right);
					nodes.offer(right);
				}
			
				Nodes up=moveUp(temp);// checking whether we can move up
				if(up.getValueG() != 0){
					if(algo==2)
						misplacedTile(up);
					else if (algo==3)
						manhattan(up);
					nodes.offer(up);
				}
				
				Nodes down=moveDown(temp);// checking whether we can move down
				if(down.getValueG() != 0){
					if(algo==2)
						misplacedTile(down);
					else if (algo==3)
						manhattan(down);
					nodes.offer(down);
				}
				// adding the expanded nodes to the list
				visitedNodes.add(temp.getPuzzle());
			}
		}
	}

	//calculating the manhattan distance
	private static void manhattan(Nodes node) {
		int h=0;
		for(int m=0;m<3;m++){
			for(int n=0;n<3;n++){
				if((node.getPuzzle()[m][n] != goalState[m][n]) && node.getPuzzle()[m][n]!=0 ){
					for(int x=0;x<3;x++){
						for(int y=0;y<3;y++){
							if(node.getPuzzle()[m][n] == goalState[x][y])
								h=h+ Math.abs(m-x)+ Math.abs(n-y);
						}
					}
				}
		
			}
		}
		node.setValueH(h);
		
	}

	//calculating the misplaced tile value
	private static void misplacedTile(Nodes node) {
		int h=0;
		for(int m=0;m<3;m++){
			for(int n=0;n<3;n++){
				if((node.getPuzzle()[m][n] != goalState[m][n]) && node.getPuzzle()[m][n]!=0 )
					h++;
			}
		}
		node.setValueH(h);
		
	}

	//checking whether the node can move left and swap the nodes
	private static Nodes moveleft(Nodes peek) {
		Nodes left = new Nodes();
		int i=0,j=0;
		int temp[][]= new int[3][3];
		for(int m=0;m<3;m++){
			for(int n=0;n<3;n++){
				temp[m][n]=peek.getPuzzle()[m][n];
				if(peek.getPuzzle()[m][n]==0){
					i=m;
					j=n;
				}
			}
		}
		if(j!=0){
			swap(temp,i,j-1,i,j);
			left.setPuzzle(temp);
			left.setValueG((peek.getValueG())+1);
			
		}
		else
			left.setValueG(0);
		return left;
	}
	

	//checking whether the node can move right and swap the nodes
	private static Nodes moveRight(Nodes peek) {
		Nodes right = new Nodes();
		int i=0,j=0;
		int temp[][]= new int[3][3];
		for(int m=0;m<3;m++){
			for(int n=0;n<3;n++){
				temp[m][n]=peek.getPuzzle()[m][n];
				if(peek.getPuzzle()[m][n]==0){
					i=m;
					j=n;
				}
			}
		}
		if(j!=2){
			swap(temp,i,j+1,i,j);
			right.setPuzzle(temp);
			right.setValueG((peek.getValueG())+1);
			
		}
		else
			right.setValueG(0);
		return right;
	}
	
	//checking whether the node can move up and swap the nodes
	private static Nodes moveUp(Nodes peek) {
		Nodes up = new Nodes();
		int i=0,j=0;
		int temp[][]= new int[3][3];
		for(int m=0;m<3;m++){
			for(int n=0;n<3;n++){
				temp[m][n]=peek.getPuzzle()[m][n];
				if(peek.getPuzzle()[m][n]==0){
					i=m;
					j=n;
				}
			}
		}
		if(i!=0){
			swap(temp,i-1,j,i,j);
			up.setPuzzle(temp);
			up.setValueG((peek.getValueG())+1);
			
		}
		else
			up.setValueG(0);
		return up;
	}
	
	//checking whether the node can move down and swap the nodes
	private static Nodes moveDown(Nodes peek) {
		Nodes down = new Nodes();
		int i=0,j=0;
		int temp[][]= new int[3][3];
		for(int m=0;m<3;m++){
			for(int n=0;n<3;n++){
				temp[m][n]=peek.getPuzzle()[m][n];
				if(peek.getPuzzle()[m][n]==0){
					i=m;
					j=n;
				}
			}
		}
		if(i!=2){
			swap(temp,i+1,j,i,j);
			down.setPuzzle(temp);
			down.setValueG((peek.getValueG())+1);
			
		}
		else
			down.setValueG(0);
		return down;
	}
	
	//swap the nodes
	private static void swap(int[][] temp, int i, int j, int i2, int j2) {
		int exchange =0;
		exchange=temp[i][j];
		temp[i][j]=temp[i2][j2];
		temp[i2][j2]=exchange;
		
	}
	
}
