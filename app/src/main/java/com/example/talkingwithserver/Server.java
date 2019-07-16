package com.example.talkingwithserver;

import android.app.ProgressDialog;
import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Response;


public class Server {
    private MainActivity callingMainActivity;
    private String loggedUserName;
    private String token;
    private String serverUrl = "http://hujipostpc2019.pythonanywhere.com/";
    private ProgressDialog loadingDialog;
    private Retrofit_Server my_server = null;


    Server(String this_user_name, MainActivity calling){
        loggedUserName = this_user_name;
        callingMainActivity = calling;
        createDialogForLoading();
        openServer();
        connectToServer();
    }


    private void createDialogForLoading(){
        loadingDialog = new ProgressDialog(callingMainActivity);
        loadingDialog.setMessage("LOADING");
        loadingDialog.setCancelable(false);
        loadingDialog.setInverseBackgroundForced(false);
    }

    private synchronized void openServer(){
        if(my_server!=null){
            return;
        }
        OkHttpClient c= new OkHttpClient();
        my_server=(new Retrofit.Builder()
                .client(c).baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build())
                .create(Retrofit_Server.class);
    }



    public synchronized void connectToServer () {
        loadingDialog.show();

        my_server.token(loggedUserName).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if(response.isSuccessful()&&response.body()!=null){
                    token=response.body().data;
                    String auth = "token "+ token;
                    //////////////////////////////////////////////////////////////////////////////////////////
                    //load information:
                    my_server.user(auth).enqueue(new Callback<UserResponse>() {
                        @Override
                        public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                            if(response.isSuccessful()&&response.body()!=null){
                                callingMainActivity.showInfo(response.body().data);
                                loadingDialog.hide();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserResponse> call, Throwable t) {
                            User incorrectUser=new User("FAILED TO LOAD","FAILED TO LOAD","");
                            callingMainActivity.showInfo(incorrectUser);
                            loadingDialog.hide();
                        }
                    });
                    //////////////////////////////////////////////////////////////////////////////////////////

                }


            }
            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                User incorrectUser=new User("FAILED TO LOAD","FAILED TO LOAD","");
                callingMainActivity.showInfo(incorrectUser);
                loadingDialog.hide();
            }

        });


    }



    public synchronized void updatePrettyName(String newName){

        loadingDialog.show();
        SetUserPrettyNameRequest pName=new SetUserPrettyNameRequest(newName);

        my_server.edit(pName,"token "+token).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.isSuccessful()&&response.body()!=null){
                    callingMainActivity.showInfo(response.body().data);
                    loadingDialog.hide();
                }

            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                User incorrectUser=new User("FAILED TO LOAD","FAILED TO LOAD","");
                callingMainActivity.showInfo(incorrectUser);
                loadingDialog.hide();
            }
        });
    }
}
