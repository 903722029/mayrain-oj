/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { JudgeInfo } from './JudgeInfo';
import type { QuestionVO } from './QuestionVO';
import type { UserVO } from './UserVO';

export type QuestionSubmitVO = {
    code?: string;
    createTime?: string;
    favourNum?: number;
    id?: number;
    judgeInfo?: JudgeInfo;
    language?: string;
    question?: QuestionVO;
    questionId?: number;
    status?: number;
    updateTime?: string;
    user?: UserVO;
    userId?: number;
};
