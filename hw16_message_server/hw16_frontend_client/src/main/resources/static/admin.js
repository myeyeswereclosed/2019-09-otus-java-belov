let stompClient = null;

const connect = () => {
  stompClient = Stomp.over(new SockJS('/users-websocket'));
  stompClient.connect({}, (frame) => {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/users/added', (userData) => addUser(JSON.parse(userData.body)));
  });
};

const clearFormValues = () => {
    $("#user-name, #user-login, #user-password").val('');
};

const addUser = (user) => {
    $("#users-list-content")
        .append(
            "<tr>" +
            "<td>" + user.id + "</td> + " +
            "<td>" + user.name + "</td> + " +
            "<td>" + user.login + "</td>" +
            "<td>" + user.role + "</td>" +
            "</tr>"
        );

    clearFormValues();
};

const createUser = () =>
    stompClient.send(
        "/app/users/create",
        {},
        JSON.stringify(
            {
                'name': $("#user-name").val(),
                'login': $("#user-login").val(),
                'password': $("#user-password").val(),
                'role': $("#user-role").val(),
            }
        )
    );

$(document).ready(connect);

$(function () {
  $("form").on('submit', (event) => {
    event.preventDefault();
  });
  $("#createNewUser").click(createUser);
});
