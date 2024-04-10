public class Main
{
    public static void main(String[] args) {
        int a=120;
        int ans=0;
        while(a!=0)
        {
            int f=a%10;
            ans=(ans*10)+f;
            a=a/10;
        }
        System.out.println(ans);
    }
}
