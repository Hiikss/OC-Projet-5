import {TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {SessionApiService} from './session-api.service';
import {Session} from '../interfaces/session.interface';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService],
    });
    service = TestBed.inject(SessionApiService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('all', () => {
    it('should retrieve all sessions', () => {
      const mockSessions: Session[] = [
        {
          id: 1,
          name: 'test',
          description: "test",
          date: new Date(),
          teacher_id: 1,
          users: [],
          createdAt: new Date(),
          updatedAt: new Date()
        }
      ];

      service.all().subscribe((sessions) => {
        expect(sessions).toEqual(mockSessions);
      });

      const req = httpTestingController.expectOne('api/session');
      expect(req.request.method).toBe('GET');
      req.flush(mockSessions);
    });
  });

  describe('detail', () => {
    it('should retrieve a session by id', () => {
      const mockSession: Session = {
        id: 1,
        name: 'test',
        description: "test",
        date: new Date(),
        teacher_id: 1,
        users: [],
        createdAt: new Date(),
        updatedAt: new Date()
      };
      const sessionId = '1';

      service.detail(sessionId).subscribe((session) => {
        expect(session).toEqual(mockSession);
      });

      const req = httpTestingController.expectOne(`api/session/${sessionId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockSession);
    });
  });

  describe('delete', () => {
    it('should delete a session by id', () => {
      const sessionId = '1';

      service.delete(sessionId).subscribe((response) => {
        expect(response).toBeNull();
      });

      const req = httpTestingController.expectOne(`api/session/${sessionId}`);
      expect(req.request.method).toBe('DELETE');
      req.flush(null);
    });
  });

  describe('create', () => {
    it('should create a new session', () => {
      const newSession: Session = {
        id: 1,
        name: 'test',
        description: "test",
        date: new Date(),
        teacher_id: 1,
        users: [],
        createdAt: new Date(),
        updatedAt: new Date()
      };

      service.create(newSession).subscribe((session) => {
        expect(session).toEqual(newSession);
      });

      const req = httpTestingController.expectOne('api/session');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(newSession);
      req.flush(newSession);
    });
  });

  describe('update', () => {
    it('should update an existing session', () => {
      const updatedSession: Session = {
        id: 1,
        name: 'test',
        description: "test",
        date: new Date(),
        teacher_id: 1,
        users: [],
        createdAt: new Date(),
        updatedAt: new Date()
      };
      const sessionId = '1';

      service.update(sessionId, updatedSession).subscribe((session) => {
        expect(session).toEqual(updatedSession);
      });

      const req = httpTestingController.expectOne(`api/session/${sessionId}`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual(updatedSession);
      req.flush(updatedSession);
    });
  });

  describe('participate', () => {
    it('should send a participate request', () => {
      const sessionId = '1';
      const userId = '100';

      service.participate(sessionId, userId).subscribe((response) => {
        expect(response).toBeUndefined();
      });

      const req = httpTestingController.expectOne(`api/session/${sessionId}/participate/${userId}`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toBeNull();
      req.flush(null);
    });
  });

  describe('unParticipate', () => {
    it('should send an unParticipate request', () => {
      const sessionId = '1';
      const userId = '100';

      service.unParticipate(sessionId, userId).subscribe((response) => {
        expect(response).toBeUndefined();
      });

      const req = httpTestingController.expectOne(`api/session/${sessionId}/participate/${userId}`);
      expect(req.request.method).toBe('DELETE');
      req.flush(null);
    });
  });
});
