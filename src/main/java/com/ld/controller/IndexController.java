/**  
 * Copyright © 2018LD. All rights reserved.
 *
 * @Title: IndexController.java
 * @Prject: videoadmin
 * @Package: com.ld.controller
 * @Description: TODO
 * @author: Myzhang  
 * @date: 2018年7月4日 下午6:33:16
 * @version: V1.0  
 */
package com.ld.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jfoenix.controls.JFXListView;
import com.ld.core.ImageExtraction;
import com.ld.core.Lodding;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * @ClassName: IndexController
 * @Description: TODO
 * @author: Myzhang  
 * @date: 2018年7月4日 下午6:33:16
 */
public class IndexController {
	
	public static Image LoddingImage = new Image(IndexController.class.getResourceAsStream("/lodding.jpg"));


	@FXML
	JFXListView<String> fileList;
	@FXML
	ImageView imageView_1;
	@FXML
	ImageView imageView_2;
	@FXML
	ImageView imageView_3;
	@FXML
	ImageView imageView_4;

	@FXML
	Text text;

	private int k=0;//计数器
	static final String video = "mp4,3gp,avi,mkv,wmv,mpg,vob,flv,swf,mov,xv,rmvb";

	private String sourceFile = "D:\\aaaaaa";

	ExecutorService service = Executors.newCachedThreadPool();

	Thread exeThread;

	private static List<File> dataList = new ArrayList<>();
	public void queryList() {
		close();
		dataList.clear();
		k=0;
		getListFile(new File(sourceFile), dataList);
		ObservableList<String> list = FXCollections.observableArrayList();	
		for (File file : dataList) {
			list.add(file.getName());
		}
		fileList.setItems(list);
	}

	public void add() throws Exception, FileNotFoundException {
		readImage();
		k++;
		k = k>=dataList.size()?0:k;
	}
	public void del() throws Exception, FileNotFoundException {
		k--;
		k = k<=0 ? dataList.size():k;
		readImage();
	}
	public synchronized void readImage() throws Exception, FileNotFoundException {
		close();
		Lodding.build().show();
		exeThread = new Thread(()->{
			try {
				long start = System.currentTimeMillis();
				System.out.println("开始:"+new Date());
				File file = dataList.get(k);
				String mag = "当前文件为:"+file.getName();
				text.setText(mag);
				new ImageExtraction(imageView_1,imageView_2,imageView_3,imageView_4).getScreenshots(file, 100,300,600,1000);
				Lodding.build().hide();
				long end = System.currentTimeMillis();
				mag+="--耗时: "+((start-end)/1000)+" 秒";
				text.setText(mag);
				System.out.println("结束:"+new Date());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		exeThread.start();
	}

	public void mvhaokan() {
		close();
		moveTotherFolders(dataList.get(k), new File("D:\\hhhhhhhhhhh"));
	}
	public void mvshiwa() {
		close();
		moveTotherFolders(dataList.get(k), new File("D:\\sssssssssss"));
	}

	private void moveTotherFolders( File startFile,File tmpFile){
		if(!tmpFile.exists()){//判断文件夹是否创建，没有创建则创建新文件夹
			tmpFile.mkdirs();
		}
		if (startFile.renameTo(new File(tmpFile.getPath()+File.separator+startFile.getName()))) {
			System.out.println("File is moved successful!");
			text.setText("移动成功");
		} else {
			text.setText("移动失败");
			System.out.println("File is failed to move!");
			System.out.println("文件移动失败！文件名：《{}》 起始路径：{}"+startFile.getName());
		}
	}


	public void getListFile(File file,List<File> dataList) {
		if(file.getPath().equals(System.getenv("windir"))) return ;
		File[] listFiles = file.listFiles();
		if(listFiles == null) return;
		for(int i=0;i<listFiles.length;i++) {
			File f = listFiles[i];
			if(!f.isDirectory()) {//文件
				String name = f.getName();
				String[] split = name.split("\\.");
				if(split.length > 1) {
					String type = name.substring(name.lastIndexOf(".") + 1);
					System.out.println("扫描:"+name);
					for(String t:video.split(",")) {//必须是视频类型
						if(t.toLowerCase().equals(type)) {
							System.out.println("匹配的:"+name);
							dataList.add(f);
							break;
						}
					}
				}
			}
			getListFile(f,dataList);
		}
	}

	public void close() {
		synchronized (dataList) {
			if(exeThread != null) {//强制关闭之前请求
				exeThread.stop();
				exeThread = null;
				System.gc();
				System.out.println("关闭垃圾");
			}
			imageView_1.setImage(null);
			imageView_2.setImage(null);
			imageView_3.setImage(null);
			imageView_4.setImage(null);
			imageView_1.setImage(LoddingImage);
			imageView_2.setImage(LoddingImage);
			imageView_3.setImage(LoddingImage);
			imageView_4.setImage(LoddingImage);
			Lodding.build().hide();
		}
	}

}
