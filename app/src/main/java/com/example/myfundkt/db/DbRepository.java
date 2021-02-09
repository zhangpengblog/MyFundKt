package com.example.myfundkt.db;

import android.os.AsyncTask;
import android.util.Log;

import com.example.myfundkt.db.dao.FoudInfoDao;
import com.example.myfundkt.db.dao.SortDao;
import com.example.myfundkt.db.entity.FoudInfoEntity;
import com.example.myfundkt.db.entity.SortEntity;
import com.example.myfundkt.utils.MyLog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DbRepository {
    private static final String TAG = "DbRepository";
    private FoudInfoDao foudInfoDao;
    private SortDao sortDao;
    public DbRepository(){
        DbMaster dbMaster = DbMaster.getDbMaster ();
        foudInfoDao = dbMaster.getFoudInfoDao ();
        sortDao =dbMaster.getSortDao ();
    }


    public void InsertInfo(FoudInfoEntity foudInfoEntity){
        Log.d (TAG, "InsertInfo: "+foudInfoEntity.id);
       new InsertAsyncTask (foudInfoDao).execute (foudInfoEntity);
    }



    private static class InsertAsyncTask extends AsyncTask<FoudInfoEntity,Void,Void> {
        private FoudInfoDao foudInfoDao;
        public InsertAsyncTask(FoudInfoDao foudInfoDao) {
            this.foudInfoDao=foudInfoDao;
        }

        @Override
        protected Void doInBackground(FoudInfoEntity... foudInfoEntities) {

              foudInfoDao.insertFoudInfo (foudInfoEntities);
              return null;
        }
    }

    public void Delete(FoudInfoEntity foudInfoEntity){
        new DeleteAsyncTask (foudInfoDao).execute (foudInfoEntity);
    }
    private static class DeleteAsyncTask extends AsyncTask<FoudInfoEntity,Void,Void>{
        private FoudInfoDao foudInfoDao;
        public DeleteAsyncTask(FoudInfoDao foudInfoDao) {
            this.foudInfoDao=foudInfoDao;
        }
        @Override
        protected Void doInBackground(FoudInfoEntity... foudInfoEntities) {
            foudInfoDao.delete (foudInfoEntities);
            return null;
        }
    }

    public int Update(FoudInfoEntity... foudInfoEntities){
        int i=0;
        try {
            i= new UpdateAsyncTask (foudInfoDao).execute (foudInfoEntities).get ();
            Log.d (TAG, "Update: "+i);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace ();
            Log.e (TAG, "Update: ", e);
        }
        return i;
    }
    private static class UpdateAsyncTask extends AsyncTask<FoudInfoEntity,Void,Integer>{
        private FoudInfoDao foudInfoDao;
        public UpdateAsyncTask(FoudInfoDao foudInfoDao) {
            this.foudInfoDao=foudInfoDao;
        }

        @Override
        protected Integer doInBackground(FoudInfoEntity... foudInfoEntities) {
            return foudInfoDao.UpdateFoudInfo (foudInfoEntities);
        }
    }

    public FoudInfoEntity FindByCode(String code){
        FoudInfoEntity entity = null;

        try {
           entity= new FindByCodeAsyncTask (foudInfoDao).execute (code).get ();
//           MyLog.d (TAG, "FindByCode: "+entity.code);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace ();
            MyLog.e (TAG, "FindByCode: ",e);
        }
        return entity;
    }
    private static class FindByCodeAsyncTask extends AsyncTask<String,Void,FoudInfoEntity>{
        private FoudInfoDao foudInfoDao;
        public FindByCodeAsyncTask(FoudInfoDao foudInfoDao) {
            this.foudInfoDao=foudInfoDao;
        }
        @Override
        protected FoudInfoEntity doInBackground(String... strings) {
            return foudInfoDao.FindByCode (strings);
        }
    }

    public List<String> GetCodes(){
        List<String> codes=new ArrayList<>();
        try {
            codes.addAll (new GetCodesAsyncTask (foudInfoDao).execute ().get ())    ;
            MyLog.d (TAG, "GetCodes: "+codes);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace ();
        }
        return codes;
    }
    private static class GetCodesAsyncTask extends AsyncTask<Void,Void,List<String>>{
        private FoudInfoDao foudInfoDao;
        public GetCodesAsyncTask(FoudInfoDao foudInfoDao) {
            this.foudInfoDao=foudInfoDao;
        }
        @Override
        protected List<String> doInBackground(Void... voids) {
            return foudInfoDao.getCodes ();
        }
    }

    public void ClearAll(){
        new ClearAsyncTask (foudInfoDao).execute ();
    }
    private static class ClearAsyncTask extends AsyncTask<Void,Void,Void>{
        private FoudInfoDao foudInfoDao;
        public ClearAsyncTask(FoudInfoDao foudInfoDao) {
            this.foudInfoDao=foudInfoDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            foudInfoDao.clear ();
            return null;
        }
    }

    public List<FoudInfoEntity> FindAll(){
        List<FoudInfoEntity> list=new ArrayList<> ();
        try {
            list.addAll (new FindAllAsyncTask (foudInfoDao).execute ().get ())  ;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace ();
        }
        return list;
    }
    private static class FindAllAsyncTask extends AsyncTask<Void,Void,List<FoudInfoEntity>>{
        private FoudInfoDao foudInfoDao;
        public FindAllAsyncTask(FoudInfoDao foudInfoDao) {
            this.foudInfoDao=foudInfoDao;
        }
        @Override
        protected List<FoudInfoEntity> doInBackground(Void... voids) {
            return foudInfoDao.findAll ();
        }
    }

    public void DeleteById(int id){
        new DeleteByIdAsyncTask (foudInfoDao).execute (id);
    }
    private static class DeleteByIdAsyncTask extends AsyncTask<Integer,Void,Void>{
        private FoudInfoDao foudInfoDao;
        public DeleteByIdAsyncTask(FoudInfoDao foudInfoDao) {
            this.foudInfoDao=foudInfoDao;
        }
        @Override
        protected Void doInBackground(Integer... integers) {
            foudInfoDao.deleteById (integers[0]);
           return null;
        }
    }


    public void InsertSort(SortEntity sortEntity){
        Log.d (TAG, "InsertSort: "+sortEntity.id);
        new InsertSortAsyncTask (sortDao).execute (sortEntity);
    }

    private static class InsertSortAsyncTask extends AsyncTask<SortEntity,Void,Void>{
        private SortDao sortDao;
        public InsertSortAsyncTask( SortDao sortDao) {
            this.sortDao=sortDao;
        }

        @Override
        protected Void doInBackground(SortEntity... sortEntities) {

            sortDao.insertSort (sortEntities);
            return null;
        }
    }


    public void ClearSort(){
        new ClearSortAsyncTask (sortDao).execute ();
    }
    private static class ClearSortAsyncTask extends AsyncTask<Void,Void,Void>{
        private SortDao sortDao;
        public ClearSortAsyncTask(SortDao sortDao) {
            this.sortDao=sortDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            sortDao.clear ();
            return null;
        }
    }
}
