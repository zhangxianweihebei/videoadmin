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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.bytedeco.javacv.FrameGrabber.Exception;

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

	private String sourceFile = "\\\\Nasf4642d\\公用\\500";

	ScheduledExecutorService service = Executors.newScheduledThreadPool(5);

	private static List<File> dataList = new ArrayList<>();
	public void queryList() {
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
		k++;
		k = k>=dataList.size()?0:k;
		readImage();
	}
	public void del() throws Exception, FileNotFoundException {
		k--;
		k = k<=0 ? dataList.size():k;
		readImage();
	}
	public void readImage() throws Exception, FileNotFoundException {
		Lodding.build().show();		
		new Thread(()->{
			try {
				File file = dataList.get(k);
				text.setText("当前文件为:"+file.getName());
				List<File> screenshots = new ImageExtraction().getScreenshots(file.getPath(), 2000,4000,6000,8000);
				imageView_1.setImage(new Image(new FileInputStream(screenshots.get(0))));
				imageView_2.setImage(new Image(new FileInputStream(screenshots.get(1))));
				imageView_3.setImage(new Image(new FileInputStream(screenshots.get(2))));
				imageView_4.setImage(new Image(new FileInputStream(screenshots.get(3))));
				Lodding.build().hide();
			} catch (Exception | FileNotFoundException e) {
				e.printStackTrace();
			}
		}).start();
	}
	public void mvhaokan() {
		moveTotherFolders(dataList.get(k), new File("\\\\Nasf4642d\\公用\\aaa\\好看"));
	}
	public void mvshiwa() {
		moveTotherFolders(dataList.get(k), new File("\\\\Nasf4642d\\公用\\aaa\\丝袜"));
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

}
