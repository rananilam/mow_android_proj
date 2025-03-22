package com.data;


import com.data.remote.IService;
import com.data.remote.RemoteDataSource;
import com.data.remote.PestService;

public class Injection {

    public static DataRepository provideDataRepository() {

        IService iService = PestService.getInstance().getIService();
        RemoteDataSource remoteDataSource = RemoteDataSource.getInstance(iService);

        return DataRepository.getInstance(remoteDataSource);
    }
}
