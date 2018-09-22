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
        VertxContext.init(vertx, new HAGlobalOption().setRto(10000).setTtl(20));
        String sendMsg = "“小说”一词最早出现于《庄子·外物》：「饰小说以干县令，其于大达亦远矣。」庄子所谓的「小说」，是指琐碎的言论，与今日小说观念相差甚远。直至东汉桓谭《新论》：「小说家合残丛小语，近取譬喻，以作短书，治身理家，有可观之辞。」班固《汉书．艺文志》将「小说家」列为十家之后，其下的定义为：「小说家者流，盖出于稗官，街谈巷语，道听途说[4]者之所造也。」才稍与今日小说的意义相近。而中国小说最大的特色，便自宋代开始具有文言小说与白话小说两种不同的小说系统。文言小说起源于先秦的街谈巷语，是一种小知小道的纪录。在历经魏晋南北朝及隋唐长期的发展，无论是题材或人物的描写，文言小说都有明显的进步，形成笔记与传奇两种小说类型。而白话小说则起源于唐宋时期说话人的话本，故事的取材来自民间，主要表现了百姓的生活及思想意识。但不管文言小说或白话小说都源远流长，呈现各自不同的艺术特色。\n"
                         + "小说的特点\n" + "价值性\n"
                         + "小说的价值本质是以时间为序列、以某一人物或几个人物为主线的，非常详细地、全面地反映社会生活中各种角色的价值关系(政治关系、经济关系和文化关系)的产生、发展与消亡过程。非常细致地、综合地展示各种价值关系的相互作用。\n"
                         + "容量性\n"
                         + "与其他文学样式相比，小说的容量较大，它可以细致地展现人物性格和人物命运，可以表现错综复杂的矛盾冲突，同时还可以描述人物所处的社会生活环境。小说的优势是可以提供整体的、广阔的社会生活。\n"
                         + "情节性\n" + "小说主要是通过故事情节来展现人物性格、表现中心的。故事来源于生活，但它通过整理、提炼和安排，就比现实生活中发生的真实实例更加集中，更加完整，更具有代表性。\n"
                         + "环境性\n"
                         + "小说的环境描写和人物的塑造与中心思想有极其重要的关系。在环境描写中，社会环境是重点，它揭示了种种复杂的社会关系，如人物的身份、地位、成长的历史背景等等。自然环境包括人物活动的地点、时间、季节、气候以及景物等等。自然环境描写对表达人物的心情、渲染环境气氛都有不少的作用。\n"
                         + "发展性\n"
                         + "小说是随着时代的发展而发展 [1]  的：魏晋南北朝，文人的笔记小说，是中国古代小说的雏形；唐代传奇的出现，尤其是三大爱情传奇，标志着古典小说的正式形成；宋元两代，随着商品经济和市井文化的发展，出现了话本小说，为小说的成熟奠定了坚实的基础；明清小说是中国古代小说发展的高峰，至今在古典小说领域内，没有可超越者，四大名著皆发于此。\n"
                         + "纯粹性\n"
                         + "纯文学中的小说体裁讲究纯粹性。“谎言去尽之谓纯。”(出自墨人钢《就是》创刊题词)便是所谓的“纯”。也就是说，小说在构思及写作的过程中能去尽政治谎言、道德谎言、商业谎言、维护阶级权贵谎言、愚民谎言等谎言，使呈现出来的小说成品具备纯粹的艺术性。小说的纯粹性是阅读者最重要的审美期待之一。随着时代的发展，不光是小说，整个文学的纯粹性逾来逾成为整个世界对文学审美的一个重要核心。";
        HASocket server = new HASocket(vertx.createDatagramSocket());
        server.acceptHandler(socket->{
            socket.handler(buffer->{
                System.out.println("server receive msg :\n"+ buffer.toString());
                try {
                    socket.write(Buffer.buffer(sendMsg+"\n" + buffer.toString()));
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

        for (int i=0 ; i<2 ; i++){
            final int id = i;
            System.out.println("init client data");
            HASocket client = new HASocket(vertx.createDatagramSocket(), "127.0.0.1" , 6666, false);
            client.handler(buf->{
                System.out.println("send id : "+id);
                System.out.println("client receive server msg  : "+id+" \n"+buf.toString());
            });

            try {
                client.write(Buffer.buffer("i am client"+id));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
