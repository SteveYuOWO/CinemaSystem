package com.littlepage.service;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.littlepage.entity.Film;
import com.littlepage.entity.FilmLike;
import com.littlepage.entity.FilmLikeCount;
import com.littlepage.entity.User;
import com.littlepage.mapper.FilmLikeMapper;
import com.littlepage.mapper.FilmMapper;
import com.littlepage.utils.PictureUtil;
import com.littlepage.utils.TimeUtils;
/**
 * 电影服务处理
 * @author 74302
 *
 */
@Service
public class FilmService {

	@Autowired
	FilmMapper filmMapper;
	
	/**
	 * 增加电影
	 * @param film
	 * @return
	 */
	public String addFilm(Film film) {
		List<Film> f=filmMapper.findByName(film.getName());
		/**
		 * 判断是否存在
		 */
		if(f.size()!=0) {
			return "该电影已经存在";
		}
		/**
		 * 格式化时间
		 */
		film.setReleaseDate(TimeUtils.formatDate(film.getReleaseDate()));
		film.setReflectDate(TimeUtils.formatDate(film.getReflectDate()));
		/**
		 * 判断时间和当前时间比
		 */
		if(film.getReleaseDate().compareTo(TimeUtils.getCurrentTime())<0) {
			System.out.println(film.getReleaseDate());
			System.out.println(TimeUtils.getCurrentTime());
			return "上映日期应该在当前日期之前";
		}
		/**
		 * 判断下映日期应该在上映日期之前
		 */
		if(film.getReflectDate().compareTo(film.getReleaseDate())<0) {
			return "下映日期输入不能在上映日期之前";
		}
		filmMapper.addFilm(film);
		return "success";
	}

	/**
	 * 查询所有电影
	 * @return
	 */
	public List<Film> findAll() {
		return filmMapper.findAll();
	}

	/**
	 * 通过id查询电影
	 * @param id
	 * @return
	 */
	public Film findById(int id) {
		List<Film> li=filmMapper.findById(id);
		if(li.size()==0) {
			return null;
		}else {
			return li.get(0);
		}
	}

	/**
	 * 保存图片，返回文件名
	 * @param posterLink
	 * @return
	 * @throws IOException 
	 */
	public String savePic(byte[] posterLink)  {
		String fileName=PictureUtil.getMD5Site(posterLink);
		try {
			PictureUtil.save(posterLink, fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName+".jpg";
	}

	/**
	 * 搜索所有有挂search的电影
	 * @param search
	 * @return
	 */
	public List<Film> search(String search) {
		return filmMapper.searchAll(search);
	}

	/**
	 * 查找符合时间的电影
	 * @param date
	 * @return
	 */
	public List<Film> findByDate(String date) {
		return filmMapper.findByDate(date);
	}

	/**
	 * 删除电影
	 * @param id
	 */
	public void delete(int id) {
		filmMapper.deleteById(id);
	}

	/**
	 * 修改电影
	 * @param id
	 * @param name
	 * @param director
	 * @param scriptWriter
	 * @param actor
	 * @param type
	 * @param location
	 * @param language
	 */
	public void modifyFilm(int id, String name, String director, String scriptWriter, String actor, String type,
			String location, String language) {
		filmMapper.modifyFilmById(id,name,director,scriptWriter,actor,type,location,language);
	}


}
