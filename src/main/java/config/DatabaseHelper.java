package config;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import entity.*;
import model.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper {
    // URL для подключения к БД
    private final String URL = "jdbc:sqlite:src\\main\\resources\\InterviewBD.db";
    // Подключение к БД
    private ConnectionSource connectionSource;
    //----------------------------------------------------------------------------
    // DAO для работы с сущностями
    private Dao<Candidate, Integer> candidateDao = null;
    private Dao<Category, Integer> categoryDao = null;
    private Dao<Interview, Integer> interviewDao = null;
    private Dao<InterviewComment, Integer> interviewCommentDao = null;
    private Dao<Interviewer, Integer> interviewerDao = null;
    private Dao<Mark, Integer> markDao = null;

    public DatabaseHelper() throws SQLException {
        connectionSource = new JdbcConnectionSource(URL);
        candidateDao = DaoManager.createDao(connectionSource,Candidate.class);
        categoryDao = DaoManager.createDao(connectionSource,Category.class);
        interviewDao = DaoManager.createDao(connectionSource,Interview.class);
        interviewCommentDao = DaoManager.createDao(connectionSource,InterviewComment.class);
        interviewerDao = DaoManager.createDao(connectionSource,Interviewer.class);
        markDao = DaoManager.createDao(connectionSource,Mark.class);
    }

    /*
     * Метод для получения всех интервью, в которых собеседовали нужного кандидата
     * @author Андрей Поляков
     * @param fio ФамилияИмяОтчество необходимого кандидата
     * @return List<Interview> все подходящие интервью
     */
    public List<Interview> getInterviewsByCandidateFio(String fio) throws SQLException {
        // первая таблица в запросе
        QueryBuilder<Interview, Integer> interviewQueryBuilder = interviewDao.queryBuilder();
        // присоединяемая таблица
        QueryBuilder<Candidate, Integer> candidateQueryBuilder = candidateDao.queryBuilder();
        // делаем выборку по полям присоединяемой таблицы
        candidateQueryBuilder.where().like("fio", fio + "%");
        // делаем left join
        interviewQueryBuilder.leftJoin(candidateQueryBuilder);
        // готово!
        // в итоге сконструирован запрос:
        // SELECT `interview`.* FROM `interview`
        // LEFT JOIN `candidate` ON `interview`.`idCandidate` = `candidate`.`idCandidate`
        // WHERE `candidate`.`fio` = 'polyakov'
        PreparedQuery<Interview> preparedQuery = interviewQueryBuilder.prepare();
        List<Interview> interviews = interviewDao.query(preparedQuery);
        return interviews;
    }

    public List<Interview> getInterviewsByDate(String date) throws SQLException {
        QueryBuilder<Interview, Integer> interviewQueryBuilder = interviewDao.queryBuilder();
        interviewQueryBuilder.where().like("Date", date + "%");
        PreparedQuery<Interview> preparedQuery = interviewQueryBuilder.prepare();
        List<Interview> interviews = interviewDao.query(preparedQuery);
        return interviews;
    }

    public List<Interview> getInterviewsByPost(String post) throws SQLException {
        QueryBuilder<Interview, Integer> interviewQueryBuilder = interviewDao.queryBuilder();
        interviewQueryBuilder.where().like("Post", post + "%");
        PreparedQuery<Interview> preparedQuery = interviewQueryBuilder.prepare();
        List<Interview> interviews = interviewDao.query(preparedQuery);
        return interviews;
    }
    //Получить по Id
    public InterviewComment getInterviewCommentByIdInterview(int id)throws SQLException
    {
        List<InterviewComment> interviewComments = interviewCommentDao.queryForAll();
        for(InterviewComment ic: interviewComments)
        {
            if(ic.getIdInterview().getIdInterview() == id)
            {
                return ic;
            }
        }
        return null;
    }
    public List<CategoryRow> getInterviewMarksAll(int idInterview)throws SQLException  {
        List<Category> categories = getCategories();
        List<Mark> marks = getInterviewMarks(idInterview);
        List<CategoryRow> categoryRows = new ArrayList<CategoryRow>();
        for(Category cat:categories) {
            CategoryRow categoryRow = new CategoryRow(cat, 0.0);
            for(Mark mark:marks)
            {
                if(mark.getIdCategory().getIdCategory() == cat.getIdCategory())
                {
                    categoryRow.setValue(mark.getValue());
                }
            }

            categoryRows.add(categoryRow);
        }
        return categoryRows;
        // TODO: 06.07.2016 отсортировать лексикографически
    }
    public Interview getInterviewById(int id) throws SQLException {
        QueryBuilder<Interview, Integer> interviewQueryBuilder = interviewDao.queryBuilder();
        interviewQueryBuilder.where().eq("idInterview", id);
        PreparedQuery<Interview> preparedQuery = interviewQueryBuilder.prepare();
        List<Interview> interviews = interviewDao.query(preparedQuery);
        if(interviews.size() == 0)
            return null;
        return interviews.get(0);
    }
    public Candidate getCandidateById(int id) throws SQLException {
        QueryBuilder<Candidate, Integer> candidateQueryBuilder = candidateDao.queryBuilder();
        candidateQueryBuilder.where().idEq(id);
        PreparedQuery<Candidate> preparedQuery = candidateQueryBuilder.prepare();
        List<Candidate> candidates = candidateDao.query(preparedQuery);
        if(candidates.size() == 0)
            return null;
        return candidates.get(0);
    }
    public Category getCategoryById(int id) throws SQLException {
        QueryBuilder<Category, Integer> query = categoryDao.queryBuilder();
        query.where().idEq(id);
        PreparedQuery<Category> preparedQuery = query.prepare();
        List<Category> categories = categoryDao.query(preparedQuery);
        if(categories.size() == 0)
            return null;
        return categories.get(0);
    }
    public Interviewer getInterviewerById(int id) throws SQLException {
        QueryBuilder<Interviewer, Integer> interviewerQueryBuilder = interviewerDao.queryBuilder();
        interviewerQueryBuilder.where().idEq(id);
        PreparedQuery<Interviewer> preparedQuery = interviewerQueryBuilder.prepare();
        List<Interviewer> interviewers = interviewerDao.query(preparedQuery);
        if(interviewers.size() == 0)
            return null;
        return interviewers.get(0);
    }
    private List <Mark> getInterviewMarks(int idInterview)throws SQLException{
        QueryBuilder<Mark, Integer> markIntegerQueryBuilder = markDao.queryBuilder();
        markIntegerQueryBuilder.where().eq("idInterview", idInterview);
        PreparedQuery<Mark> preparedQuery = markIntegerQueryBuilder.prepare();
        List<Mark> marks = markDao.query(preparedQuery);
        return marks;
    }
    //Получить всех
    public List<Category> getCategories() throws SQLException {
        return categoryDao.queryForAll();
    }
    public List<Candidate> getCandidates() throws SQLException {
        return candidateDao.queryForAll();
    }
    public List<Interview> getInterview() throws SQLException{
        return interviewDao.queryForAll();
    }
    public List<Interviewer> getInterviewers() throws SQLException {
        return interviewerDao.queryForAll();
    }
    //Добавить
   public void addInterview(String candidate, String interviewer, String date, String result, String post)  throws SQLException{
//        Interview interview = new Interview();
//        QueryBuilder<Interview, Integer> interviewQueryBuilder = interviewDao.queryBuilder();
//        interviewQueryBuilder.where().eq("FIO", candidate);
//        PreparedQuery<Interview> preparedQuery = interviewQueryBuilder.prepare();
//        List<Interview> interviews = interviewDao.query(preparedQuery);
//        return interviews;
//
//
//        interview.setIdCandidate(getCandidateById(idCandidate));
//        interview.setIdInterviewer(getInterviewerById(idInterviewer));
//        interview.setDate(date);
//        interview.setResult(result);
//        interview.setPost(post);
//        interviewDao.create(interview);
    }
    public void addInterviewer(String fio)  throws SQLException{
        Interviewer interviewer = new Interviewer();
        interviewer.setFio(fio);
        // TODO: 05.07.2016 Что делать при неудачной вставке? Исключение или возвращать false?
        interviewerDao.create(interviewer);
    }
    public void addCategory(String name)  throws SQLException{
        Category category = new Category();
        category.setName(name);
        categoryDao.create(category);
    }
    public void addMark(int idCategory, int idInterview, double value)  throws SQLException{
        //Перед добавлением оценки, убедись, что создано интервью!
        Mark mark = new Mark();
        mark.setIdCategory(getCategoryById(idCategory));
        mark.setIdInterview(getInterviewById(idInterview));
        mark.setValue(value);
        markDao.create(mark);
    }
    public void addInterviewComment(int idInterview, String experience, String recommendations, String lastWork, String comment)throws SQLException{
        InterviewComment iCom = new InterviewComment();
        iCom.setIdInterview(getInterviewById(idInterview));
        iCom.setExperience(experience);
        iCom.setRecommendations(recommendations);
        iCom.setLastWork(lastWork);
        iCom.setComment(comment);
        interviewCommentDao.create(iCom);
    }
    public void addCandidate(String fio, String date, String banned)  throws SQLException{
        Candidate candidate = new Candidate();
        candidate.setFio(fio);
        candidate.setBornDate(date);
        candidate.setBanned(banned);
        candidateDao.create(candidate);
    }
    //Удалить
    public void delCategoryById(int id)  throws SQLException{
        Category category = getCategoryById(id);
        categoryDao.delete(category);
    }
    public void delInterviewById(int id)  throws SQLException{
        Interview interview = getInterviewById(id);
        QueryBuilder<InterviewComment, Integer> query = interviewCommentDao.queryBuilder();
        query.where().eq("idInterview", id);
        PreparedQuery<InterviewComment> preparedQuery = query.prepare();
        List<InterviewComment> interviewComment = interviewCommentDao.query(preparedQuery);
        if(interviewComment.size() != 0){
            interviewCommentDao.delete(interviewComment.get(0));
        }
        for(Mark mark:interview.getMarks()){
            markDao.delete(mark);
        }
        interviewDao.delete(interview);
    }
    public void delCandidateById(int id)  throws SQLException{
        Candidate candidate = getCandidateById(id);
        candidateDao.delete(candidate);
    }
    //Редактировать
    public void editCategory(int id, String name)throws SQLException{
        Category cat = getCategoryById(id);
        cat.setName(name);
        categoryDao.createOrUpdate(cat);
    }
    public void editInterviewDate(int idInterview, String date)throws SQLException{
        Interview interview = getInterviewById(idInterview);
        interview.setDate(date);
        interviewDao.createOrUpdate(interview);
    }
    public void editInterviewResult(int idInterview, String result)throws SQLException{
        Interview interview = getInterviewById(idInterview);
        interview.setResult(result);
        interviewDao.createOrUpdate(interview);
    }
    public void editInterviewPost(int idInterview, String post)throws SQLException{
        Interview interview = getInterviewById(idInterview);
        interview.setResult(post);
        interviewDao.createOrUpdate(interview);
    }
    public void editInterviewInterviewer(int idInterview, int idInterviewer)throws SQLException{
        Interview interview = getInterviewById(idInterview);
        interview.setIdInterviewer(getInterviewerById(idInterviewer));
        interviewDao.createOrUpdate(interview);
    }
}
