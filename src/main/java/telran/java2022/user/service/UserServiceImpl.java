package telran.java2022.user.service;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java2022.user.dao.UsersRepository;
import telran.java2022.user.dto.LoginDto;
import telran.java2022.user.dto.RoleDto;
import telran.java2022.user.dto.UserDto;
import telran.java2022.user.dto.UserRegisterDto;
import telran.java2022.user.dto.UserUpdate;
import telran.java2022.user.exception.NotValidPassowordException;
import telran.java2022.user.exception.UserAlreadyExistException;
import telran.java2022.user.exception.UserNotFoundException;
import telran.java2022.user.model.User;

@Service	
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	final UsersRepository usersRepository;
	final ModelMapper modelMapper;

	@Override
	public UserDto userRegister(UserRegisterDto userRegisterDto) {
		if(usersRepository.existsById(userRegisterDto.getLogin())) {
			throw new UserAlreadyExistException(userRegisterDto.getLogin());
		}
		User user = modelMapper.map(userRegisterDto, User.class);
		usersRepository.save(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto userLogin(LoginDto loginDto) {
		User user = usersRepository.findById(loginDto.getLogin())
				.orElseThrow(()->new UserNotFoundException(loginDto.getLogin()));
		if(!user.validatePassword(loginDto.getPassword())) {
			throw new NotValidPassowordException();
		}
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto userDelete(String login) {
		User user = usersRepository.findById(login).orElseThrow(()->new UserNotFoundException(login));
		usersRepository.deleteById(login);	
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto userUpdate(String login, UserUpdate userUpdate) {
		User user = usersRepository.findById(login).orElseThrow(()->new UserNotFoundException(login));
		user.setFirstName(userUpdate.getFirstName());
		user.setLastName(userUpdate.getLastName());
		usersRepository.save(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public RoleDto addRole(String login, String role) {
		User user = usersRepository.findById(login).orElseThrow(()->new UserNotFoundException(login));
		user.addRole(role);
		usersRepository.save(user);
		return modelMapper.map(user, RoleDto.class);
	}

	@Override
	public RoleDto deleteRole(String login, String role) {
		User user = usersRepository.findById(login).orElseThrow(()->new UserNotFoundException(login));
		user.deleteRole(role);
		usersRepository.save(user);
		return modelMapper.map(user, RoleDto.class);
	}

	@Override
	public void changePassword(LoginDto loginDto) {
		User user = usersRepository.findById(loginDto.getLogin())
				.orElseThrow(()->new UserNotFoundException(loginDto.getPassword()));
		user.setPassword(loginDto.getPassword());

	}

	@Override
	public UserDto getUser(String login) {
		User user = usersRepository.findById(login).orElseThrow(()->new UserNotFoundException(login));
		return modelMapper.map(user, UserDto.class);
	}

}
