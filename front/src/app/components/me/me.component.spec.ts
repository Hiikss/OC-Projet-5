import {ComponentFixture, TestBed} from '@angular/core/testing';
import {of} from 'rxjs';
import {Router} from '@angular/router';
import {MatSnackBar} from '@angular/material/snack-bar';
import {MeComponent} from './me.component';
import {SessionService} from '../../services/session.service';
import {UserService} from '../../services/user.service';
import {User} from '../../interfaces/user.interface';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let mockRouter: jest.Mocked<Router>;
  let mockSessionService: jest.Mocked<SessionService>;
  let mockMatSnackBar: jest.Mocked<MatSnackBar>;
  let mockUserService: jest.Mocked<UserService>;

  beforeEach(async () => {
    mockRouter = {
      navigate: jest.fn(),
    } as unknown as jest.Mocked<Router>;

    mockSessionService = {
      sessionInformation: {id: 1},
      logOut: jest.fn(),
    } as unknown as jest.Mocked<SessionService>;

    mockMatSnackBar = {
      open: jest.fn(),
    } as unknown as jest.Mocked<MatSnackBar>;

    mockUserService = {
      getById: jest.fn(),
      delete: jest.fn(),
    } as unknown as jest.Mocked<UserService>;

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      providers: [
        {provide: Router, useValue: mockRouter},
        {provide: SessionService, useValue: mockSessionService},
        {provide: MatSnackBar, useValue: mockMatSnackBar},
        {provide: UserService, useValue: mockUserService},
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('should fetch user data on init', () => {
      const mockUser: User = {
        id: 1,
        email: "test@test.com",
        lastName: "lastname",
        firstName: "firstname",
        admin: false,
        password: '12345',
        createdAt: new Date(),
        updatedAt: new Date()
      };
      mockUserService.getById.mockReturnValue(of(mockUser));

      component.ngOnInit();

      expect(mockUserService.getById).toHaveBeenCalledWith('1');
      expect(component.user).toEqual(mockUser);
    });
  });

  describe('back', () => {
    it('should navigate back when back() is called', () => {
      jest.spyOn(window.history, 'back');
      component.back();
      expect(window.history.back).toHaveBeenCalled();
    });
  });

  describe('delete', () => {
    it('should delete the user and logout', () => {
      mockUserService.delete.mockReturnValue(of(null));

      component.delete();

      expect(mockUserService.delete).toHaveBeenCalledWith('1');
      expect(mockMatSnackBar.open).toHaveBeenCalledWith(
        'Your account has been deleted !',
        'Close',
        {duration: 3000}
      );
      expect(mockSessionService.logOut).toHaveBeenCalled();
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
    });
  });
});
