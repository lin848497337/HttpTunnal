import g.HAGlobalOption;
import g.HASocket;
import g.VertxContext;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;

/**
 * @author chengjin.lyf on 2018/9/22 上午8:07
 * @since 1.0.25
 */
public class Test {

    public static void main(String args[]){
        Vertx vertx = Vertx.vertx();
        VertxContext.init(vertx, new HAGlobalOption().setRto(1000).setTtl(2));
        HASocket server = new HASocket(vertx.createDatagramSocket());
        server.acceptHandler(socket->{
            socket.handler(buffer->{
                System.out.println("server receive msg "+ buffer.toString());
                try {
                    socket.write(Buffer.buffer("this is server response , do you receive me!"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        server.listen(6666);

        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        HASocket client = new HASocket(vertx.createDatagramSocket(), "127.0.0.1" , 6666);
        client.handler(buf->{
            System.out.println("client receive server msg : "+buf.toString());
            client.close();
        });
        try {
            client.write(Buffer.buffer("this is client msg , your should hear me!"));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
