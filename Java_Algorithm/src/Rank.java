
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rank {
    public static String[] reRank(String[] input){
        int r=0,b=0,g=0;
        for(String s : input){
            if(s.equals("r")){
                r+=1;
            }else if(s.equals("b")){
                b+=1;
            }else if(s.equals("g")){
                g+=1;
            }
        }
        String[] ans = new String[input.length];
        for (int i = 0; i < r; i++) {
            ans+="r";
        }
        for (int i = 0; i < g; i++) {
            ans+="g";
        }
        for (int i = 0; i < b; i++) {
            ans+="b";
        }
//        return new String[0];
        return ans;
    }

    public static String[] reRankTwoPointers(String[] input){
        if(input == null || input.length == 0){
            return new String[0];
        }
        int i = 0;
        int left = 0;
        int right = input.length-1;
        while(i<=right){
            if(input[i].equals("r")){
                String t1 = input[i];
                input[i] = input[left];
                input[left] = t1;
                i++;
                left++;
            }else if(input[i].equals("g")){
                i++;
            }else if(input[i].equals("b")){
                String t2 = input[i];
                input[i] = input[right];
                input[right] = t2;
                right--;
            }
        }
        return input;
    }


    public List<String> binaryTreePaths(TreeNode root){
        List<String> paths = new ArrayList<>();
        if(root == null){
            return paths;
        }

        List<String> leftPaths = binaryTreePaths(root.left);
        List<String> rightPaths = binaryTreePaths(root.right);

        for(String path:leftPaths){
            paths.add(root.val + "->" + path);
        }

        for(String path:leftPaths){
            paths.add(root.val + "->" + path);
        }

        if(paths.size()==0){
            paths.add(""+root.val);
        }
        return paths;
    }

    public static void main(String[] args) {
        String[] input = new String[]{"r","r","b","g","b","r","g"};
        Arrays.asList(reRankTwoPointers(input)).forEach(e-> System.out.print(e));
    }
}
