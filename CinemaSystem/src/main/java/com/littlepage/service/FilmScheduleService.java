package com.littlepage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.littlepage.entity.Film;
import com.littlepage.entity.FilmSchedule;
import com.littlepage.mapper.FilmMapper;
import com.littlepage.mapper.FilmScheduleMapper;
import com.littlepage.utils.TimeUtils;

@Service
public class FilmScheduleService {
	
	@Autowired
	FilmScheduleMapper filmScheduleMapper;
	
	@Autowired
	FilmMapper filmMapper;
	
	/**
	 * 根据日期进行查找电影排班
	 * @param date
	 * @return
	 */
	public List<FilmSchedule> findByDate(String date) {
		return filmScheduleMapper.findByDate(date);
	}

	/**
	 * 增加电影排班,false为时间段重复
	 * @param film
	 * @param filmSchedule
	 */
	public boolean addSchedule(String filmName, FilmSchedule filmSchedule) {
		List<Film> film=filmMapper.findByName(filmName);
		filmSchedule.setFid(film.get(0).getId()+"");
		
		//如果这个时间段有时间重复,直接返回失败
		List<FilmSchedule> li=filmScheduleMapper.findRepeat(filmSchedule);
		if(li.size()!=0) {
			return false;
		}
		//前中后插入30个座位
		filmSchedule.setView("30");
		filmScheduleMapper.addFilmSchedule(filmSchedule);
		return true;
	}
	/**
	 * 展示列表
	 * @param startDate
	 * @param endDate
	 * @param filmRoom
	 * @param sightView
	 * @return
	 */
	public List<FilmSchedule> showList(String startDate, String endDate, String filmRoom, String sightView) {
		List<FilmSchedule> list=null;
		if(sightView.equals("影厅")) {
			list=filmScheduleMapper.showByFilmRoom(startDate,endDate,filmRoom);
		}else {
			list=filmScheduleMapper.showByFilm(startDate,endDate,filmRoom);
		}
		return list;
	}

	/**
	 * 设置开始和结束时间
	 * @param id
	 * @param startDate
	 * @param endDate
	 */
	public void setStartDateAndEndDate(String id, String startDate, String endDate) {
		filmScheduleMapper.setStartDateAndEndDate(id,startDate,endDate);
	}

	/**
	 * 通过id删除
	 * @param id
	 */
	public void deleteById(String id) {
		filmScheduleMapper.deleteById(id);
	}

	/**
	 * 通过id查询电影排班，并且在可见范围内
	 * @param fid
	 */
	public List<FilmSchedule> findById(int fid) {
		String date=TimeUtils.getCurrentTime();
		List<FilmSchedule> li=filmScheduleMapper.findById(fid,date);
		return li;
	}

	/**
	 * 通过id查询电影安排
	 * @param id
	 * @return
	 */
	public List<FilmSchedule> findByFilmScheduleId(int id) {
		return filmScheduleMapper.findByFilmScheduleId(id);
	}

}
