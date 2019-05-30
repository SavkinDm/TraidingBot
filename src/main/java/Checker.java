public class Checker {
        Long epoch1;


        public boolean checkTime(Long LastrequestTime){
            boolean ans;
            epoch1 = System.currentTimeMillis() / 1000;
            if (Math.abs(epoch1 - LastrequestTime)>91){
               ans = false;
            }else{
                ans = true;
            }


        return ans;}


}
