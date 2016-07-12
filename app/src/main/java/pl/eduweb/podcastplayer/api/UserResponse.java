package pl.eduweb.podcastplayer.api;

/**
 * Created by Autor on 2016-07-05.
 */
public class UserResponse {

    public String username;
    public String objectId;
    public String email;
    public String firstName;
    public String lastName;
    public String sessionToken;


    //{"ACL":{"*":{"read":true},"cFoEFZ1XBK":{"read":true,"write":true}},"objectId":"cFoEFZ1XBK","username":"test@test.com","updatedAt":"2016-07-06T08:53:13.925Z","createdAt":"2016-07-06T08:52:26.742Z","email":"test@test.com","firstName":"Adam","lastName":"Testowy","sessionToken":"r:750185456bff998f866ccbc7fda634cd"}


    @Override
    public String toString() {
        return "UserResponse{" +
                "username='" + username + '\'' +
                ", objectId='" + objectId + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", sessionToken='" + sessionToken + '\'' +
                '}';
    }
}
