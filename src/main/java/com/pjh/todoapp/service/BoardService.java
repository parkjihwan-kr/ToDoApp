package com.pjh.todoapp.service;


import com.pjh.todoapp.Entity.board.Board;
import com.pjh.todoapp.Entity.user.User;
import com.pjh.todoapp.Repository.BoardRepository;
import com.pjh.todoapp.Repository.UserRepository;
import com.pjh.todoapp.security.UserDetailsImpl;
import com.pjh.todoapp.security.UserDetailsServiceImpl;
import com.pjh.todoapp.util.ApiRequestException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    @Transactional
    public Board 게시글작성(Board board, UserDetailsImpl userDetails){
        String username = userDetails.getUser().getUsername();
        String userPassword = userDetails.getUser().getPassword();

        Optional<User> userList = userRepository.findByUsername(username);
        System.out.println("게시글 작성 서비스");
        userList.stream().forEach((user)->
                System.out.println("user.getUsername() :"+user.getUsername()+"user.getPassword() : "+user.getPassword())
        );
        board.setUsername(username);
        board.setPassword(userPassword);
        Board boardEntity = boardRepository.save(board);
        List<Board> myBoardList = boardRepository.findAll();
        int testId = 3;
        // List<Board> myBoardList2 = boardRepository.findByBoardId(id);
        // 이건 커스텀화 해야하는디?
        int myId = 0;
        //Board board222 = boardRepository.findById(myId).orElseThrow();
        // 이거는 orElseThrow()필수임 이유 까먹음... ㅋㅋㅋ

        return boardEntity;
    }
    @Transactional
    public Map<String, List<String>> 유저투두카드조회() {
        Map<String, List<String>> todoUserList = new HashMap<>();
        List<Board> boards = 게시판모든리스트조회();

        for (Board board : boards) {
            String username = board.getUsername();
            String title = board.getTitle();

            todoUserList.computeIfAbsent(username, key -> new ArrayList<>()).add(title);
        }

        // userTitles 맵의 모든 필드를 출력
        /*for (Map.Entry<String, List<String>> entry : userTitles.entrySet()) {
            String username = entry.getKey();
            List<String> titles = entry.getValue();

            System.out.println("Username: " + username);
            System.out.println("Titles: " + titles);
        }*/

        return todoUserList;
    }

    @Transactional
    public List<Board> 게시판모든리스트조회(){
        // System.out.println("게시판 모든 리스트 조회 서비스");
        Sort sort = Sort.by(Sort.Order.desc("id"));
        // "id"를 기준으로 내림차순 정렬
        List<Board> boardList = boardRepository.findAll(sort);
        // 이를 통해 해당 username에 대한 title만 뽑아서 버튼형식으로 title을 누르면 detail이 열리는 형태로
        return boardList;
    }

    @Transactional
    public Board 게시글조회(int id){
        return boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 게시글을 찾을 수 없습니다: " + id));
    }

    @Transactional
    public Board 게시글수정(int id, Board user){

        Board userEntity = boardRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        //System.out.println("userEntity.getPassword()"+userEntity.getPassword()+" user.getPassword() : "+user.getPassword());

        if (!userEntity.getPassword().equals(user.getPassword())) {
            throw new ApiRequestException("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }
        userEntity.setTitle(user.getTitle());
        userEntity.setContents(user.getContents());
        user.setCreatedDate(user.getCreatedDate());
        boardRepository.save(userEntity);
        return userEntity;
    }
    @Transactional
    public void 게시글삭제(Board user, int id) {
        Optional<Board> userOptional = boardRepository.findById(id);

        if (userOptional.isPresent()) {
            Board userInDatabase = userOptional.get();
            String userPassword = userInDatabase.getPassword();

            if (userPassword.equals(user.getPassword())) {
                boardRepository.deleteById(id);
            } else {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        } else {
            throw new EntityNotFoundException("해당 ID의 게시글을 찾을 수 없습니다: " + id);
        }
    }
}

