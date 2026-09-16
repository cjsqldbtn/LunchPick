import React, { useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import axios from 'axios';
import "../css/PasswordReset.css";

const PasswordReset = () => {
    const [params] = useSearchParams();
    const navigate = useNavigate();

    const memberId = params.get('memberId');
    const key = params.get('key');

    const [password, setPassword] = useState('');
    const [passwordCheck, setPasswordCheck] = useState('');
    const [loading, setLoading] = useState(false);
    const [success, setSuccess] = useState(false);

    const handleSubmit = (e) => {
        e.preventDefault();
        if (!memberId || !key) {
            alert('잘못된 비밀번호 변경 링크입니다.');
            return;
        }
        if (password.length < 8) {
            alert('비밀번호는 8자 이상 입력해주세요.');
            return;
        }
        if (password !== passwordCheck) {
            alert('비밀번호가 일치하지 않습니다.');
            return;
        }
        setLoading(true);
        axios.post('/member/password/change', {
            memberId: memberId,
            key: key,
            password: password
        })
        .then(res => {
            setSuccess(true);
        })
        .catch(err => {
            console.error('비밀번호 변경 실패 : ', err);
            alert(
                err.response?.data ||
                '유효하지 않거나 만료된 링크입니다.'
            );
        })
        .finally(() => {
            setLoading(false);
        });
    };

    return (
        <div className="password-reset-page">
            <div className="password-reset-card">
                {!success ? (
                    <>
                        <div className="password-reset-header">
                            <div className="password-reset-brand">
                                🔐
                            </div>
                            <span className="password-reset-eyebrow">
                                NEW PASSWORD
                            </span>
                            <h2>
                                새로운 비밀번호를 설정해주세요
                            </h2>
                            <p>
                                앞으로 로그인할 때 사용할<br />
                                새로운 비밀번호를 입력해주세요.
                            </p>
                        </div>
                        <form className="password-reset-form" onSubmit={handleSubmit}>
                            <div className="password-reset-form-group">
                                <label htmlFor="password">
                                    NEW PASSWORD
                                </label>
                                <input
                                    type="password"
                                    id="password"
                                    value={password}
                                    placeholder="새 비밀번호를 입력해주세요"
                                    onChange={(e) =>
                                        setPassword(e.target.value)
                                    }
                                    minLength={8}
                                    autoComplete="new-password"
                                    required
                                />
                                <span className="password-reset-help">
                                    8자 이상 입력해주세요.
                                </span>
                            </div>
                            <div className="password-reset-form-group">
                                <label htmlFor="passwordCheck">
                                    CONFIRM PASSWORD
                                </label>
                                <input
                                    type="password"
                                    id="passwordCheck"
                                    value={passwordCheck}
                                    placeholder="비밀번호를 다시 입력해주세요"
                                    onChange={(e) => setPasswordCheck(e.target.value)}
                                    minLength={8}
                                    autoComplete="new-password"
                                    required
                                />
                                {passwordCheck && (
                                    <span className={password === passwordCheck ? 'password-match' : 'password-not-match'}>
                                        {password === passwordCheck 
											? '✓ 비밀번호가 일치합니다.'
                                            : '비밀번호가 일치하지 않습니다.'
                                        }
                                    </span>
                                )}
                            </div>
                            <button className="password-reset-submit" type="submit" disabled={loading}>
                                {loading ? '변경하는 중...' : '비밀번호 변경'}
                                {!loading && <span>→</span>}
                            </button>
                        </form>
                    </>
                ) : (
                    <div className="password-reset-success">
                        <div className="password-reset-brand">
                            🎉
                        </div>
                        <span className="password-reset-eyebrow">
                            PASSWORD CHANGED
                        </span>
                        <h2>
                            비밀번호가 변경되었습니다!
                        </h2>
                        <p>
                            새로운 비밀번호로<br />
                            로그인해주세요.
                        </p>
                        <button
                            className="password-reset-submit"
                            type="button"
                            onClick={() => navigate('/')}>
                            로그인하러 가기
                            <span>→</span>
                        </button>
                    </div>
                )}
            </div>
        </div>
    );
};

export default PasswordReset;