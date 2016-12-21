import test from 'ava';
import request from 'supertest';
import app from '../../server';

test('aa', t => request(app).delete('/system').expect(200).then(res => res.body).then(console.log));