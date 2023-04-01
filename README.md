![Liferary Banner](https://user-images.githubusercontent.com/14046092/228737335-f016bbd6-0722-481e-a1bb-025a0af0af67.png)
# What is Liferary?

> Project name "Liferary" created by combining "Life" and "Library".  
It means a library with knowledges for a better life.

<div align="justify">
  <b>"Liferary"</b> is a free knowledge-sharing service that combines the concept of wikis, where users can make free contributions, with paid online education services. The platform's key feature is that "knowledge sharers" can publish their knowledge in various forms, such as text, images, and videos (via YouTube), and share them with others. Knowledge sharers can also use Google Meet, a real-time video conferencing service, to conduct study sessions. Users can learn from the shared knowledge and express their opinions through the community service. Liferary aims to enable as many people as possible to lead better lives by sharing knowledge with one another.
</div>
<br/>

## Sustainable Development Goals
![goals](https://user-images.githubusercontent.com/14046092/228918938-da18180b-5a4f-46bd-b75b-1a9cd4c33e98.png)
* **[Goal 3 : Good Health and Well-Being](https://www.un.org/sustainabledevelopment/health/)**
  * "Knowledge sharers" can share their excellent knowledge on topics such as how to purify water, take medication correctly, and exercise safely in Liferary. By doing so, they can help more people live healthier and better lives.
* **[Goal 4 : Quality Education](https://www.un.org/sustainabledevelopment/education/)**
  * Our ultimate goal for the service is to create an environment where shared knowledge leads to high-quality learning. To achieve this, we are planning to establish procedures to certify knowledge sharers as experts in their knowledge and attract more professional knowledge sharers. For now, our service is providing an environment where people can share their unique knowledge, learn together in real-time, and communicate with each other about knowledge for better learning.
* **[Goal 10 : Reduced Inequalities](https://www.un.org/sustainabledevelopment/inequality/)**
  * For some, even the most mundane information may be unfamiliar and difficult on the opposite side of the world. Educational disparities lead to global inequality, and to solve this, we must provide equal learning opportunities to everyone. We aim to provide an environment where anyone with Internet access can learn completely for free.
<br/>

## Project Repositories
We seperate repositories to prevent unexpected side effects in each other's code.
* [Web Repository](https://github.com/GDSC-SKHU/liferary-frontend)
* [App Repository](https://github.com/GDSC-SKHU/liferary-mobile)
* [Server Repository](https://github.com/GDSC-SKHU/liferary-backend)
<br/>

## Technologies
* **Web**
  * React.js
  * Next.js
  * styled-components
* **App**
  * Flutter
* **Server**
  * Spring Boot
  * Spring Data JPA
  * Spring Security
  * MySQL
  * Redis
  * Docker
  * Swagger
    * [Liferary API](http://api-liferary.duckdns.org/swagger-ui/index.html/)
  * **Google Cloud Platform**
  * **Firebase**
<br/>

## Demo Video
[![Frame 7 (1)](https://user-images.githubusercontent.com/14046092/229252135-e6ef32ae-09b5-44b9-a460-1beeb73f6c2c.png)](https://youtu.be/Ch0Fxq5tXzw)  
<br/>

## How to use Liferary
### Web
### 1. First, access the service through the provided [link](https://liferary-frontend.vercel.app/).
![Main](https://user-images.githubusercontent.com/14046092/229236590-a5aaf0ca-cb93-4f7c-9595-3fa0ed09cb1d.png)
![MainRecent](https://user-images.githubusercontent.com/14046092/229236596-0d15c908-14f4-40df-9d81-0e4f2158f56b.png)

Now, you can read all posts in Liferary! But, if you want to share your knowledge, you need to sign up and log in.  

### 2. You can sign up and login with any email you have or just use Google OAuth2.0 login.
![SignUp](https://user-images.githubusercontent.com/14046092/229236600-338a56f7-4e58-4c83-a245-d099fc1424bd.png)
![Login](https://user-images.githubusercontent.com/14046092/229236587-30e179f2-1fe4-45ad-b00c-b4f3478a69e5.png)
![OAuth](https://user-images.githubusercontent.com/14046092/229236599-0e87eb69-862f-438f-b8d7-3dbdeb0c4529.png)  

### 3. If you success to login, you can find write knowledge share post button in main page.
![MainLogined](https://user-images.githubusercontent.com/14046092/229236592-6e2e73f7-eccd-42b1-9736-a1ddc40e37f8.png)  

### 4. This is an input form for posts.
![KS](https://user-images.githubusercontent.com/14046092/229236585-aeb9a957-59d7-4e13-a688-713058578d49.png) 
![SCFP](https://user-images.githubusercontent.com/14046092/229250879-f3001f6f-5a51-4218-aa6d-1bba3d6adbce.png) 

You can share your knowledge with first form. You can upload text, images, even Youtube video. Writing a post is most important activity. The study and community post input forms are the same in format with knowledge share post, but the study and community input forms can't selecting a category and inserting a YouTube link.  

### 5. If you success to submit a post, you may see the following result screens:
![Knowledge](https://user-images.githubusercontent.com/14046092/229236582-02486ce5-c899-4220-898e-ded3389fd41f.png)  

You can navigate from the page where you have shared your knowledge to the form for creating a study post. Additionally, you can also check the community board for other users' opinions about the knowledge you have shared.  

![Study](https://user-images.githubusercontent.com/14046092/229236601-a281cc6e-9a51-441e-937c-4ecfa3f56a69.png)  
 
You can notice the real-time lecture by Google Meet in study post.  

![Community](https://user-images.githubusercontent.com/14046092/229236576-02836df6-1177-46ea-a196-5401e436cc08.png)  

In community posts, you can add comments to discuss about the knowledge with other users.  

### 6. You can see the list of knowledge share posts.
![Lists](https://user-images.githubusercontent.com/14046092/229236586-11148756-b622-4144-a21c-9aa82c5a9990.png)  

Also, you can check knowledge share posts by category, based on your interests.  

### 7. Search knowledge share posts by keywords you want to learn! 

![Search](https://user-images.githubusercontent.com/14046092/229250080-b76392c8-df11-4625-a16d-12d6cf3bb06e.png)  

### 8. In My Page, you can see your account's information and posts that you wrote.

![MyPage](https://user-images.githubusercontent.com/14046092/229236598-57e73388-5a17-447b-9856-17659db03dd3.png)
<br/>  

### App
#### Liferary Flutter app has not been deployed yet because the technical implementation has not been completed. But you can follow the instructions below to try out the app in your local environment up to the completed part after clone this repository. [App Repository](https://github.com/GDSC-SKHU/liferary-mobile)

### 1. If you use Android device.
You need to have Android SDK version 31 or later installed. After launching Android Studio, enter the following commands sequentially in the terminal window.
```dart
flutter pub get
flutter doctor -v
flutter run
```

### 2. If you use iOS device.
You need to have iOS version 11 or later installed. After launching iOS simulator, enther the following commands sequentially in the terminal window.
```dart
flutter pub get
flutter doctor -v
flutter run
```

* If you encounter the 'Error running pod install' error during iOS build, follow the steps below:
1. Open a terminal window for your project and enter `cd ios` to move to the iOS folder.
2. Enter `pod repo update` to update the repositories.
3. Enter `pod install` to install the pod files again.
<br/>  

## Next Steps
* Implementing a certification process for knowledge providers' expertise.
* Improving UI/UX for users.
<br/>  

## Team Liferary
|[Hangil Lee](https://github.com/hangillee)|[Sinyoung Park](https://github.com/ParkSY0919)|[Eunji Lee](https://github.com/Lee2Eunji)|[Jiyun Lee](https://github.com/dd-jiyun)|
|:---:|:---:|:---:|:---:|
|<img src="https://github.com/hangillee.png">|<img src="https://github.com/ParkSY0919.png">|<img src="https://github.com/Lee2Eunji.png">|<img src="https://github.com/dd-jiyun.png">|
|Backend|App Frontend|Web Frontend|Backend|
