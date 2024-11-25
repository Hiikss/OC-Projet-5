import {ComponentFixture, TestBed} from '@angular/core/testing';
import {FormComponent} from './form.component';
import {FormBuilder, ReactiveFormsModule} from '@angular/forms';
import {MatSnackBar} from '@angular/material/snack-bar';
import {ActivatedRoute, Router} from '@angular/router';
import {of} from 'rxjs';
import {SessionService} from '../../../../services/session.service';
import {TeacherService} from '../../../../services/teacher.service';
import {SessionApiService} from '../../services/session-api.service';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let mockRouter: any;
  let mockSnackBar: any;
  let mockSessionApiService: any;
  let mockSessionService: any;
  let mockTeacherService: any;
  let mockRoute: any;

  beforeEach(async () => {
    mockRouter = {
      navigate: jest.fn(),
      url: '/sessions',
    };

    mockSnackBar = {
      open: jest.fn(),
    };

    mockSessionApiService = {
      detail: jest.fn(),
      create: jest.fn(),
      update: jest.fn(),
    };

    mockSessionService = {
      sessionInformation: {admin: true},
    };

    mockTeacherService = {
      all: jest.fn().mockReturnValue(of([])),
    };

    mockRoute = {
      snapshot: {
        paramMap: {
          get: jest.fn(),
        },
      },
    };

    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule],
      declarations: [FormComponent],
      providers: [
        FormBuilder,
        {provide: Router, useValue: mockRouter},
        {provide: MatSnackBar, useValue: mockSnackBar},
        {provide: SessionApiService, useValue: mockSessionApiService},
        {provide: SessionService, useValue: mockSessionService},
        {provide: TeacherService, useValue: mockTeacherService},
        {provide: ActivatedRoute, useValue: mockRoute},
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('should redirect non-admin users to /sessions', () => {
      mockSessionService.sessionInformation.admin = false;
      component.ngOnInit();
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
    });

    it('should initialize form for new session', () => {
      mockRouter.url = '/sessions/create';
      component.ngOnInit();
      expect(component.onUpdate).toBe(false);
      expect(component.sessionForm?.value).toEqual({
        name: '',
        date: '',
        teacher_id: '',
        description: '',
      });
    });

    it('should fetch session details and initialize form in update mode', () => {
      const mockSession = {
        name: 'Session',
        date: '2024-11-22T00:00:00Z',
        teacher_id: '1',
        description: 'Description',
      };
      mockRoute.snapshot.paramMap.get.mockReturnValue('123');
      mockSessionApiService.detail.mockReturnValue(of(mockSession));

      mockRouter.url = '/sessions/update/123';
      component.ngOnInit();

      expect(component.onUpdate).toBe(true);
      expect(mockSessionApiService.detail).toHaveBeenCalledWith('123');
      expect(component.sessionForm?.value).toEqual({
        name: 'Session',
        date: '2024-11-22',
        teacher_id: '1',
        description: 'Description',
      });
    });
  });

  describe('submit', () => {
    beforeEach(() => {
      component.sessionForm = component['fb'].group({
        name: ['Session'],
        date: ['2024-11-22'],
        teacher_id: ['1'],
        description: ['Description'],
      });
    });

    it('should create a session and navigate to /sessions', () => {
      mockSessionApiService.create.mockReturnValue(of({}));
      component.onUpdate = false;

      component.submit();

      expect(mockSessionApiService.create).toHaveBeenCalledWith({
        name: 'Session',
        date: '2024-11-22',
        teacher_id: '1',
        description: 'Description',
      });
      expect(mockSnackBar.open).toHaveBeenCalledWith(
        'Session created !',
        'Close',
        {duration: 3000}
      );
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });

    it('should update a session and navigate to /sessions', () => {
      mockSessionApiService.update.mockReturnValue(of({}));
      component.onUpdate = true;
      component['id'] = '123';

      component.submit();

      expect(mockSessionApiService.update).toHaveBeenCalledWith('123', {
        name: 'Session',
        date: '2024-11-22',
        teacher_id: '1',
        description: 'Description',
      });
      expect(mockSnackBar.open).toHaveBeenCalledWith(
        'Session updated !',
        'Close',
        {duration: 3000}
      );
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });

  describe('exitPage', () => {
    it('should show a snack bar message and navigate to /sessions', () => {
      component['exitPage']('Test Message');
      expect(mockSnackBar.open).toHaveBeenCalledWith(
        'Test Message',
        'Close',
        {duration: 3000}
      );
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });
});
