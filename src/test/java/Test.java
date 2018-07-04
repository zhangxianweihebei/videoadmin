import com.ld.core.ImageExtraction;

import java.io.File;
import java.util.Date;
import java.util.List;

public class Test {
    public static void main(String[] str)throws Exception{

        System.out.println("开始:"+new Date());
        List<File> images = new ImageExtraction().getScreenshots("d:/456.mkv",200,300,500);
//        images.forEach(e->System.out.println(e.getName()));
        System.out.println(images.size());
        System.out.println("结束:"+new Date());
    }
}
