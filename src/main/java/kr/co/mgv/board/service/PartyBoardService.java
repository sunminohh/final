package kr.co.mgv.board.service;

import org.springframework.stereotype.Service;

import kr.co.mgv.board.mapper.PartyBoardDao;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PartyBoardService {

	private final PartyBoardDao partyBoardDao;
}
