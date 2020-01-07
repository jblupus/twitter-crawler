package com.jblupus.twittercrawler;

import com.jblupus.twitter.crawler.jobs.*;
import com.jblupus.twittercrawler.jobs.*;
import inf.ufg.br.jblupus.jobs.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

/**
 * Created by joao on 11/28/16.
 */
@Configuration
@ComponentScan
public class Application {
    private static final Logger logger = Logger.getLogger(Application.class.getName());
    private static AnnotationConfigApplicationContext context;

    static AnnotationConfigApplicationContext getContext() {
        return context;
    }


    public static void main(String[] args) {
        if (args!=null && args.length>0) {
            logger.log(Level.INFO, "Crawler option {0}", new Object[]{args[0]});

            switch (args[0]) {
                default:
                case "5":
                    crawler();
                    break;
                case "0":
                    seedSaver();
                    break;
                case "1":
                    crawlerScheduler();
                    break;
                case "2":
                    friendsCollector();
                    break;
                case "3":
                    likesCollector();
                    break;
                case "4":
                    tweetsCollector();
                    break;

                case "6":
                    resume();
                    break;

                case "7":
                    context.getBean(ResumeInfo.class).run();
                    break;
                case "8":
                    context.getBean(StatusResumer.class).run();
                    break;
            }
        } else {
            seedSaver();
            crawlerScheduler();
        }

        if(context !=null ){
            context.destroy();
            context.close();
        }
    }

    private static void resume() {
        SeedsResumer r = context.getBean(SeedsResumer.class);
        try {
            r.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void crawler() {
        Crawler crawler = context.getBean(Crawler.class);
        crawler.run();
    }

    private static void tweetsCollector() {
        TweetsCollector tc = context.getBean(TweetsCollector.class);
        tc.run();
    }

    private static void likesCollector() {
        LikesCollector lc = context.getBean(LikesCollector.class);
        lc.run();
    }

    private static void friendsCollector() {
        FriendshipCollector fc = context.getBean(FriendshipCollector.class);
        fc.run();
    }

    private static void crawlerScheduler() {
        CrawlerScheduler cs = context.getBean(CrawlerScheduler.class);
        cs.run();
    }

    private static void seedSaver() {
        SeedsSaver ss = context.getBean(SeedsSaver.class);
        ss.run();
    }

    static void buildContext() {
        context = new AnnotationConfigApplicationContext();
        context.scan("inf.ufg.br");
        context.refresh();
    }
}